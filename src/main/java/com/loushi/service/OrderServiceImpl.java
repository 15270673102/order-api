package com.loushi.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loushi.component.enumeration.order.OrderPayStatusEnu;
import com.loushi.component.enumeration.order.OrderStatusEnu;
import com.loushi.component.enumeration.task.TaskStatusEnu;
import com.loushi.component.enumeration.user.UserAppealRoleEnu;
import com.loushi.component.enumeration.user.UserStatusEnu;
import com.loushi.constant.SystemInfo;
import com.loushi.mapper.*;
import com.loushi.model.*;
import com.loushi.util.StringUtil;
import com.loushi.utils.MathUtils;
import com.loushi.utils.task.TaskkUtil;
import com.loushi.vo.order.*;
import com.loushi.vo.user.CommissionRecordVO;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单表
 * @author 技术部
 */

@Service
@Slf4j
@Transactional(noRollbackFor = Exception.class)
public class OrderServiceImpl implements IOrderService {

    @Resource
    private UserOrderMapper orderMapper;
    @Resource
    private UserTaskMapper taskMapper;
    @Resource
    private UsersMapper userMapper;
    @Resource
    private UserPayMapper payMapper;
    @Resource
    private UserPlatformMapper userPlatformMapper;
    @Resource
    private UserAppealMapper userAppealMapper;
    @Resource
    private UserOrderCheckMapper orderCheckMapper;
    @Resource
    private TaskkUtil taskkUtil;


    @Override
    public List<RemitRecordListVO> remitRecordList(Integer uid) {
        List<RemitRecordListVO> listVos = payMapper.remitRecordList(uid, OrderPayStatusEnu.yet_balance.getValue());
        if (listVos.size() == 0)
            return null;
        return listVos;
    }

    @Override
    public List<UserAppeal> appealList(Integer orderId) {
        return userAppealMapper.appealList(orderId);
    }


    @Override
    @Synchronized
    public JoinOrderVO joinOrder(Integer uid, Integer taskId, Integer num) throws Exception {
        Users user = userMapper.selectByPrimaryKey(uid);
        if (user.getUserStatus() != UserStatusEnu.bing.getValue())
            return new JoinOrderVO("1", "还没有绑定(个人资料),请去个人中心绑定...", null, null);

        UserTask task = taskMapper.selectByPrimaryKey(taskId);
        int count = userPlatformMapper.selectByUidPlatFormType(uid, task.getPlatform());
        if (count == 0)
            return new JoinOrderVO("2", "还没有绑定(平台资料),请去个人中心绑定...", null, null);

        if (task.getTaskStatus() == TaskStatusEnu.finish.getValue())
            throw new Exception("该订单已经完成了...");
        if (task.getTaskStatus() == TaskStatusEnu.cancel.getValue())
            throw new Exception("该订单主动取消了...");

        List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(uid, task.getPlatform());
        if (num > userPlatforms.size())
            throw new Exception(String.format("最多接%s单", userPlatforms.size()));

        //判断接单的数量小于库存剩余的数量
        if (num > task.getRemainNum())
            throw new Exception("任务数量不足...");


        //创建一个订单
        UserOrder order = new UserOrder();
        order.setUserId(uid);
        order.setTaskId(taskId);
        order.setStartTime(new Date());
        order.setOrderNum(num);
        order.setOrderPrice(task.getPrice());
        order.setOrderStatus(OrderStatusEnu.process.getValue());
        order.setCreateTime(new Date());
        order.setOrderNo(MathUtils.orderNum());
        orderMapper.insertSelective(order);

        //任务数量的减少
        int remainNum = task.getRemainNum() - num;
        task.setRemainNum(remainNum);
        if (remainNum == 0) {
            task.setTaskStatus(TaskStatusEnu.complete.getValue());
            task.setCancelTime(new Date());
        }
        taskMapper.updateByPrimaryKeySelective(task);

        //时间差
        DateTime endTime = DateUtil.offsetMinute(task.getPublishTime(), task.getConsumeTime());
        if (DateUtil.currentSeconds() >= endTime.getTime())
            throw new Exception("该任务异常...不建议接该单");

        //计算剩余时间
        String time;
        long minutes = DateUtil.between(new Date(), endTime, DateUnit.MINUTE);
        if (minutes > 0)
            time = StrUtil.format("{}分钟", minutes);
        else {
            long second = DateUtil.between(new Date(), endTime, DateUnit.SECOND);
            time = StrUtil.format("{}秒", second);
        }
        return new JoinOrderVO(SystemInfo.success_code, SystemInfo.success, task.getTitle(), time);
    }


    @Override
    public PageInfo<UserOrder> userOrderList(Integer uid, UserOrderVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());

        List<UserOrder> list;
        if (vo.getState() == OrderStatusEnu.all.getValue())
            list = orderMapper.selectByStateUid(uid, null);

        else if (vo.getState() == OrderStatusEnu.sys_check_faild.getValue())
            list = orderMapper.selectByCheckFaild(uid,
                    OrderStatusEnu.sys_check_faild.getValue(),
                    OrderStatusEnu.person_check_faild.getValue(),
                    OrderStatusEnu.person_appealing.getValue(),
                    OrderStatusEnu.sys_2_check_faild.getValue());
        else
            list = orderMapper.selectByStateUid(uid, vo.getState());

        for (UserOrder order : list) {
            orderItemCons(order);
        }
        return new PageInfo<>(list);
    }


    @Override
    public UserOrder orderItem(Integer orderId) {
        UserOrder order = orderMapper.selectByPrimaryKey(orderId);
        orderItemCons(order);

        //做任务号详情
        UserTask task = taskMapper.selectByPrimaryKey(order.getTaskId());
        Users joinOrderUser = userMapper.selectByPrimaryKey(order.getUserId());
        DoTaskItemVO doTaskItem = taskkUtil.getDoTaskItem(task, joinOrderUser);
        order.setDoTaskItemVO(doTaskItem);

        //order-check
        List<UserOrderCheck> userOrderChecks = orderCheckMapper.selectByOrderIdStatus(
                order.getId(),
                OrderStatusEnu.sys_check_faild.getValue(),
                OrderStatusEnu.artificial_audit_faild.getValue(),
                OrderStatusEnu.person_check_faild.getValue()
        );
        for (UserOrderCheck userOrderCheck : userOrderChecks) {
            userOrderCheck.setCheckStatusStr(OrderStatusEnu.getByValue(userOrderCheck.getCheckStatus()));
        }
        order.setUserOrderChecks(userOrderChecks);
        return order;
    }

    //订单详情公共数据
    private void orderItemCons(UserOrder order) {
        UserTask task = taskMapper.selectByPrimaryKey(order.getTaskId());
        task.setTaskformStr(taskkUtil.getTaskForm(task).toString());
        order.setTask(task);
        order.setOrderStatusStr(OrderStatusEnu.getByValue(order.getOrderStatus()));
        //最新申述时间
        Date maxDate = userAppealMapper.selectByOrderIdMaxCreateTime(order.getId(), UserAppealRoleEnu.user.getValue());
        order.setAppealTime(maxDate);
    }


    @Override
    public PageInfo<CommissionListVO> commissionList(Integer uid, Integer pageNum, Integer pageSize, String filterTime) {
        PageHelper.startPage(pageNum, pageSize);
        List<CommissionListVO> commissionListVOS = payMapper.selectByUidTime(uid, filterTime);
        PageInfo<CommissionListVO> pageInfo = new PageInfo<>(commissionListVOS);

        Iterator<CommissionListVO> iterator = pageInfo.getList().iterator();
        while (iterator.hasNext()) {
            CommissionListVO vo = iterator.next();

            if (OrderPayStatusEnu.freeze.getValue() == vo.getPayStatus()) {
                iterator.remove();
                continue;
            }
            UserTask task = taskMapper.selectByPrimaryKey(vo.getTaskId());
            vo.setTask(task);
        }
        return pageInfo;
    }


    @Override
    public CommissionRecordVO commissionRecord(Integer uid) {
        List<CommissionRecordModelVO> list = orderMapper.selectByUid(uid);
        Users user = userMapper.selectByPrimaryKey(uid);

        BigDecimal totalIncome = new BigDecimal(0);
        BigDecimal delayIncome = new BigDecimal(0);
        String msg = null;

        for (CommissionRecordModelVO commissionRecordModelVO : list) {
            //还没有完成的订单过滤掉
            if (null == commissionRecordModelVO.getPayStatus())
                continue;

            int payStatus = commissionRecordModelVO.getPayStatus();

            //提示信息
            if (payStatus == OrderPayStatusEnu.balanceing.getValue())
                msg = "(结算中)";

            if (payStatus == OrderPayStatusEnu.yet_balance.getValue())
                totalIncome = totalIncome.add(new BigDecimal(commissionRecordModelVO.getMoney().toString()));

            else if (payStatus == OrderPayStatusEnu.balanceing.getValue() || payStatus == OrderPayStatusEnu.no_balance.getValue())
                delayIncome = delayIncome.add(new BigDecimal(commissionRecordModelVO.getMoney().toString()));
        }
        return new CommissionRecordVO(totalIncome, delayIncome, msg, user.getNickName(), DateUtil.formatChineseDate(new Date(), false));
    }


    @Override
    public void saveOrderCheckFaild(Integer uid, SaveOrderCheckFaildVO vo) throws Exception {
        //修改订单申述状态
        UserOrder order = orderMapper.selectByUidOrderId(uid, vo.getOrderId());
        if (order.getOrderStatus() == OrderStatusEnu.person_appealing.getValue())
            throw new Exception("重复点击了...");

        order.setOrderStatus(OrderStatusEnu.person_appealing.getValue());
        orderMapper.updateByPrimaryKeySelective(order);

        //UserAppeal
        UserAppeal userAppeal = new UserAppeal();
        userAppeal.setOrderId(vo.getOrderId());
        userAppeal.setRole(UserAppealRoleEnu.user.getValue());
        userAppeal.setContent(StringUtil.replaceMysqlCharacters(vo.getAppealExplain()));
        userAppeal.setPics(vo.getPics());
        userAppeal.setCreateTime(new Date());
        userAppealMapper.insertSelective(userAppeal);

        //insertOrderCheck
        UserOrderCheck userOrderCheck = new UserOrderCheck();
        userOrderCheck.setOrderId(order.getId());
        userOrderCheck.setCheckStatus(OrderStatusEnu.person_appealing.getValue());
        userOrderCheck.setCheckReason(vo.getAppealExplain());
        userOrderCheck.setCreateTime(new Date());
        orderCheckMapper.insertSelective(userOrderCheck);
    }

}
