package com.loushi.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loushi.component.enumeration.task.TaskJoinEnu;
import com.loushi.component.enumeration.task.TaskPlatformEnu;
import com.loushi.component.enumeration.task.TaskStatusEnu;
import com.loushi.mapper.UserOrderMapper;
import com.loushi.mapper.UserPlatformMapper;
import com.loushi.mapper.UserTaskMapper;
import com.loushi.model.UserOrder;
import com.loushi.model.UserPlatform;
import com.loushi.model.UserTask;
import com.loushi.service.rabbitmq.SenderMsg;
import com.loushi.service.redis.IRedisService;
import com.loushi.util.StringUtil;
import com.loushi.utils.MathUtils;
import com.loushi.utils.commonPython.PythonHttpApi;
import com.loushi.utils.task.TaskkUtil;
import com.loushi.utils.weibo.WeiboLinkUtil;
import com.loushi.vo.commonPython.WeiboContentVO;
import com.loushi.vo.task.BozhuTaskListVO;
import com.loushi.vo.task.PublishTaskRabbitVO;
import com.loushi.vo.task.PublishTaskVO;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 任务表
 * @author 技术部
 */

@Service
@Slf4j
@Transactional(noRollbackFor = Exception.class)
public class TaskServiceImpl implements ITaskService {

    @Resource
    private UserTaskMapper taskMapper;
    @Resource
    private SenderMsg senderMsg;
    @Resource
    private PythonHttpApi pythonHttpApi;
    @Resource
    private UserOrderMapper orderMapper;
    @Resource
    private UserPlatformMapper userPlatformMapper;
    @Resource
    private IRedisService redisService;
    @Resource
    private TaskkUtil taskkUtil;


    @Override
    public UserTask orderItem(Integer uid, Integer taskId) {
        UserTask task = taskMapper.selectByPrimaryKey(taskId);
        if (TaskStatusEnu.publishing.getValue() == task.getTaskStatus()) {
            task.setCancelTime(DateUtil.offsetMinute(task.getPublishTime(), task.getConsumeTime()));
        }

        List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(uid, task.getPlatform());
        task.setJoinNum(userPlatforms.size());

        //计算完成数量
        task.setFinishNum(task.getTotalNum() - task.getRemainNum());
        task.setTaskformStr(taskkUtil.getTaskForm(task).toString());
        return getTaskType(uid, task);
    }


    @Override
    public PageInfo<UserTask> selectTaskAll(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserTask> lists = taskMapper.selectTaskAll(TaskStatusEnu.publishing.getValue());
        PageInfo<UserTask> pageInfo = new PageInfo<>(lists);

        Iterator<UserTask> iterator = pageInfo.getList().iterator();
        while (iterator.hasNext()) {
            UserTask task = iterator.next();

            DateTime endTime = DateUtil.offsetMinute(task.getPublishTime(), task.getConsumeTime());
            if (DateUtil.currentSeconds() >= endTime.getTime()) {
                iterator.remove();
                continue;
            }

            long minutes = DateUtil.between(new Date(), endTime, DateUnit.MINUTE);
            if (minutes == 0)
                task.setRemainTime(1L);
            else
                task.setRemainTime(minutes);

            //计算完成数量
            task.setFinishNum(task.getTotalNum() - task.getRemainNum());
            task.setTaskformStr(taskkUtil.getTaskForm(task).toString());
            getTaskType(uid, task);

            task.setPlatformStr(TaskPlatformEnu.getByValue(task.getPlatform()));
        }
        return pageInfo;
    }


    //计算任务的类型(能不能接单)
    private UserTask getTaskType(Integer uid, UserTask task) {
        //判断这个任务是不是接过了
        int count = orderMapper.selectByUidTaskId(uid, task.getId());
        if (count == 0)
            task.setTaskType(TaskJoinEnu.able_join.getValue());
        else
            task.setTaskType(TaskJoinEnu.no_join.getValue());
        return task;
    }


    @Override
    @Synchronized
    public void cancelTask(Integer taskId, Integer uid) throws Exception {
        UserTask task = taskMapper.selectByPaublishTaskId(uid, taskId);
        if (task.getTaskStatus() != TaskStatusEnu.publishing.getValue()) throw new Exception("该订单已经撤销过了");

        task.setTaskStatus(TaskStatusEnu.cancel.getValue());
        task.setCancelTime(new Date());
        taskMapper.updateByPrimaryKeySelective(task);
    }


    @Override
    public void publishTask(PublishTaskVO vo, Integer uid) throws Exception {
        String msgId = WeiboLinkUtil.getWeiboContentMsgId(vo.getSourceLink());

        //爬取微博内容
        Object data = pythonHttpApi.getWeiboContent(msgId).getData();
        if (data == null)
            throw new Exception("这条数据标题未获取到,请检查链接...");

        WeiboContentVO weiboContentVO = JSONObject.parseObject(data.toString(), WeiboContentVO.class);
        UserTask task = new UserTask();
        task.setReserveDay(vo.getReserveDay());
        task.setTaskDesc(vo.getDesc());
        task.setTitle(StringUtil.replaceMysqlCharacters(weiboContentVO.getPublish_msg()));
        task.setPlatform(vo.getPlatform());
        task.setTaskform(vo.getTaskform().toString());
        task.setSourceLink(String.format("https://weibo.com/%s/%s", weiboContentVO.getPublisher_uid(), weiboContentVO.getMsg_bid()));
        task.setPrice(new BigDecimal(vo.getPrice()).doubleValue());
        task.setTotalNum(vo.getTotalNum());
        task.setRemainNum(vo.getTotalNum());
        task.setConsumeTime(vo.getConsumeTime());
        task.setTaskStatus(TaskStatusEnu.publishing.getValue());
        task.setUserId(uid);
        task.setTaskNo(MathUtils.taskNum());
        task.setPublishTime(new Date());
        task.setCreateTime(new Date());
        taskMapper.insertSelective(task);

        //发布保留时间redis监听
        redisService.publishTask(task, vo);

        //发消息到延迟队列中
        senderMsg.publishTask(msgId, task, vo);
    }


    @Override
    public PageInfo<UserTask> selectBozhuTaskList(Integer uid, BozhuTaskListVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<UserTask> lists;

        Integer taskStatus = vo.getTaskStatus();
        if (taskStatus == TaskStatusEnu.all.getValue())
            lists = taskMapper.selectBozhuTaskList(uid, null, vo.getFilterTime());
        else if (taskStatus == TaskStatusEnu.complete.getValue())
            lists = taskMapper.selectBozhuTaskList(uid, TaskStatusEnu.complete.getValue(), vo.getFilterTime());
        else
            lists = taskMapper.selectBozhuTaskList(uid, taskStatus, vo.getFilterTime());

        for (UserTask task : lists) {
            task.setFinishNum(task.getTotalNum() - task.getRemainNum());
            task.setTaskformStr(taskkUtil.getTaskForm(task).toString());
            task.setPlatformStr(TaskPlatformEnu.getByValue(task.getPlatform()));
        }
        return new PageInfo<>(lists);
    }


    @Override
    @Async
    public void publishTaskConsumer(PublishTaskRabbitVO vo) {
        UserTask task = taskMapper.selectByPrimaryKey(vo.getTaskId());

        //审核微博任务
        if (task.getPlatform() == TaskPlatformEnu.weibo.getValue()) {
            //task
            if (task.getTaskStatus() == TaskStatusEnu.publishing.getValue()) {
                task.setTaskStatus(TaskStatusEnu.finish.getValue());
                task.setCancelTime(new Date());
                taskMapper.updateByPrimaryKeySelective(task);
            }

            //orders
            List<UserOrder> orders = orderMapper.selectBytaskId(task.getId());
            for (UserOrder order : orders) {
                order.setSubmitTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
            }
            log.info("taskId【{}】下面的所有订单自动提交完成...", vo.getTaskId());
        }

        //发消息到延迟队列中
        senderMsg.oneSysAuditDelayTime(vo);
    }

}
