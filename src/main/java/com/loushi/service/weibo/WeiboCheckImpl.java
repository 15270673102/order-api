package com.loushi.service.weibo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.loushi.component.contants.CommonPythonCons;
import com.loushi.component.contants.OrderCons;
import com.loushi.component.enumeration.order.OrderPayStatusEnu;
import com.loushi.component.enumeration.order.OrderStatusEnu;
import com.loushi.component.enumeration.task.TaskPubEnu;
import com.loushi.component.enumeration.task.TaskStatusEnu;
import com.loushi.mapper.*;
import com.loushi.model.*;
import com.loushi.utils.DateUtils;
import com.loushi.utils.MathUtils;
import com.loushi.utils.weibo.WeiboAuditUtil;
import com.loushi.utils.weibo.WeiboLinkUtil;
import com.loushi.vo.task.PublishTaskRabbitVO;
import com.loushi.vo.util.AgainCrawlVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class WeiboCheckImpl implements IWeiboCheck {

    @Resource
    private UserOrderCrawlCommentMapper commentMapper;
    @Resource
    private UserOrderCrawlForwardMapper forwardMapper;
    @Resource
    private UserOrderCrawlLikeMapper likeMapper;
    @Resource
    private WeiboAuditUtil weiboAuditUtil;
    @Resource
    private UserOrderCheckMapper orderCheckMapper;
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


    //==================================== weibo 审核 =========================================================

    //2审(不保存数据，之对比数据在不在了)
    @Override
    @Async
    public void secondAuditWeiboTask(Integer taskId) throws Exception {
        log.info("开始2审【微博任务】 taskId【{}】，任务保留时间到了...", taskId);
        //2审失败的订单列表
        Set<Integer> abnormalOrderSet = new HashSet<>();
        //订单列表
        Set<Integer> ordersSet = new HashSet<>();

        UserTask task = taskMapper.selectByPrimaryKey(taskId);
        String msgId = WeiboLinkUtil.getWeiboContentMsgId(task.getSourceLink());
        List<UserOrder> orders = orderMapper.selectBytaskId(task.getId());

        //任务订单集合
        for (UserOrder order : orders) {
            ordersSet.add(order.getId());
        }

        //这个时间应该是爬取的最大时间
        task.setCancelTime(DateUtils.calculateFirstAuditWeiboTaskCancelDatetime(task));

        //爬取微博(转赞评) 对比第一次
        JSONArray taskForms = JSONObject.parseArray(task.getTaskform());
        for (Object taskForm : taskForms) {
            Set<String> newSet = new HashSet<>();

            //评论对比
            if (TaskPubEnu.comment.getValue() == (Integer) taskForm) {
                //第2次爬取数据
                newSet = weiboAuditUtil.weiboCommentData2(task, msgId, CommonPythonCons.start_page, newSet);

                //第1次审核数据
                //订单结合
                for (UserOrder order : orders) {
                    if (order.getOrderStatus() != OrderStatusEnu.finish.getValue()) {
                        continue;
                    }
                    //绑定平台
                    List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(order.getUserId(), task.getPlatform());
                    for (UserPlatform userPlatform : userPlatforms) {
                        //评论数据
                        List<UserOrderCrawlComment> commentList = commentMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid());
                        for (UserOrderCrawlComment userOrderCrawlComment : commentList) {
                            //1审数据不在了
                            if (!newSet.contains(userOrderCrawlComment.getMsgId())) {
                                abnormalOrderSet.add(order.getId());
                            }
                        }
                    }
                }
            }

            //点赞对比
            if (TaskPubEnu.like.getValue() == (Integer) taskForm) {
                newSet = weiboAuditUtil.weiboLikeData2(task, msgId, CommonPythonCons.start_page, newSet);
                for (UserOrder order : orders) {
                    if (order.getOrderStatus() != OrderStatusEnu.finish.getValue()) {
                        continue;
                    }
                    List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(order.getUserId(), task.getPlatform());
                    for (UserPlatform userPlatform : userPlatforms) {
                        List<UserOrderCrawlLike> userOrderCrawlLikes = likeMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid());
                        for (UserOrderCrawlLike userOrderCrawlLike : userOrderCrawlLikes) {
                            if (!newSet.contains(userOrderCrawlLike.getMsgId())) {
                                abnormalOrderSet.add(order.getId());
                            }
                        }
                    }
                }
            }

            //转发对比
            if (TaskPubEnu.forward.getValue() == (Integer) taskForm) {
                newSet = weiboAuditUtil.weiboForwordData2(task, msgId, CommonPythonCons.start_page, newSet);
                for (UserOrder order : orders) {
                    if (order.getOrderStatus() != OrderStatusEnu.finish.getValue()) {
                        continue;
                    }
                    List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(order.getUserId(), task.getPlatform());
                    for (UserPlatform userPlatform : userPlatforms) {
                        List<UserOrderCrawlForward> userOrderCrawlForwards = forwardMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid());
                        for (UserOrderCrawlForward userOrderCrawlForward : userOrderCrawlForwards) {
                            if (!newSet.contains(userOrderCrawlForward.getMsgId())) {
                                abnormalOrderSet.add(order.getId());
                            }
                        }
                    }
                }
            }
        }

        //异常订单集合
        for (Integer abnormalOrderId : abnormalOrderSet) {
            ordersSet.remove(abnormalOrderId);

            log.info("当前订单 orderId【{}】, 2审【失败】", abnormalOrderId);
            sys2CheckFaild(abnormalOrderId);

            //判断order_pay是否有数据
            UserPay userPay = payMapper.selectByOrderId(abnormalOrderId);
            if (userPay == null) {
                continue;
            }

            if (OrderPayStatusEnu.no_balance.getValue() == userPay.getPayStatus() || OrderPayStatusEnu.balanceing.getValue() == userPay.getPayStatus()) {
                userPay.setPayStatus(OrderPayStatusEnu.freeze.getValue());
                payMapper.updateByPrimaryKeySelective(userPay);
            }
        }

        //正常订单
        for (Integer orderId : ordersSet) {
            UserOrder order = orderMapper.selectByPrimaryKey(orderId);

            Integer orderStatus = order.getOrderStatus();
            if (orderStatus <= OrderStatusEnu.finish.getValue()) {
                log.info("当前订单 orderId【{}】 orderStatus【{}】 完成了, 跳过2审", order.getId(), orderStatus);
                sys2CheckSkip(order.getId(), OrderCons.order_weibo_sys_2_ckeck_skip);
                continue;
            }

            UserPay userPay = payMapper.selectByOrderId(order.getId());
            Integer payStatus = userPay.getPayStatus();
            if (OrderPayStatusEnu.yet_balance.getValue() == payStatus) {
                log.info("当前订单 orderId【{}】结算完成 payStatus【{}】, 跳过审核 ", order.getId(), payStatus);
                sys2CheckSkip(order.getId(), OrderCons.order_weibo_sys_2_ckeck_skip_2);
                continue;
            }

            log.info("当前订单 orderId【{}】, 2审【成功】", orderId);
            sys2CheckSuccess(orderId);
        }
        log.info("taskId【{}】,2审微博任务结束...", taskId);
    }


    //1审
    @Override
    @Async
    public void firstAuditWeiboTask(PublishTaskRabbitVO vo) {
        UserTask task = taskMapper.selectByPrimaryKey(vo.getTaskId());
        log.info("开始 1审【微博任务】 taskId【{}】", task.getId());

        //任务下面的订单
        Set<String> AccountSet = new HashSet<>();
        List<UserOrder> orders = orderMapper.selectBytaskId(task.getId());
        for (UserOrder order : orders) {
            //获取所有的任务账号集合
            List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(order.getUserId(), task.getPlatform());
            for (UserPlatform userPlatform : userPlatforms) {
                AccountSet.add(userPlatform.getAccountUid());
            }
        }

        //这个时间应该是爬取的最大时间
        task.setCancelTime(DateUtils.calculateFirstAuditWeiboTaskCancelDatetime(task));

        //爬取微博(转赞评)
        JSONArray taskForms = JSONObject.parseArray(task.getTaskform());
        for (Object taskForm : taskForms) {
            if (TaskPubEnu.comment.getValue() == (Integer) taskForm) {
                log.info("开始爬取微博【评论】数据...");
                weiboAuditUtil.weiboCommentData(task, vo.getMsgId(), CommonPythonCons.start_page, AccountSet);
            }
            if (TaskPubEnu.like.getValue() == (Integer) taskForm) {
                log.info("开始爬取微博【点赞】数据...");
                weiboAuditUtil.weiboLikeData(task, vo.getMsgId(), CommonPythonCons.start_page, AccountSet);
            }

            if (TaskPubEnu.forward.getValue() == (Integer) taskForm) {
                log.info("开始爬取微博【转发】数据...");
                weiboAuditUtil.weiboForwordData(task, vo.getMsgId(), CommonPythonCons.start_page, AccountSet);
            }
        }

        //审核数据
        for (UserOrder order : orders) {
            //统计数据
            int commentTotalCount = 0;
            int likeTotalCount = 0;
            int forwardTotalCount = 0;
            List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(order.getUserId(), task.getPlatform());
            for (UserPlatform userPlatform : userPlatforms) {
                commentTotalCount += commentMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid()).size();
                likeTotalCount += likeMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid()).size();
                forwardTotalCount += forwardMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid()).size();
            }

            //对比数据是否正常
            log.info("开始1审核 orderId【{}】,订单数据", order.getId());
            Integer orderNum = order.getOrderNum();
            StringBuilder sb = new StringBuilder();
            List<Boolean> list = new ArrayList<>();

            if (taskForms.contains(TaskPubEnu.comment.getValue())) {
                list.add(commentTotalCount >= orderNum);
                sb.append(String.format(" 评论%s/%s", commentTotalCount, orderNum));
            }
            if (taskForms.contains(TaskPubEnu.like.getValue())) {
                list.add(likeTotalCount >= orderNum);
                sb.append(String.format(" 点赞%s/%s", likeTotalCount, orderNum));
            }
            if (taskForms.contains(TaskPubEnu.forward.getValue())) {
                list.add(forwardTotalCount >= orderNum);
                sb.append(String.format(" 转发%s/%s", forwardTotalCount, orderNum));
            }
            log.info("当前订单 orderId【{}】，完成进度【{}】", order.getId(), sb);

            //结果
            if (list.contains(false)) {
                log.info("当前订单 orderId【{}】,系统1审失败", order.getId());
                StringBuilder sb1 = new StringBuilder();

                if (taskForms.contains(TaskPubEnu.comment.getValue())) {
                    sb1.append(String.format(" 评论【%s/%s】 ", commentTotalCount, orderNum));
                }
                if (taskForms.contains(TaskPubEnu.like.getValue())) {
                    sb1.append(String.format(" 点赞【%s/%s】 ", likeTotalCount, orderNum));
                }
                if (taskForms.contains(TaskPubEnu.forward.getValue())) {
                    sb1.append(String.format(" 转发【%s/%s】 ", forwardTotalCount, orderNum));
                }
                sys1CheckFaild(order.getId(), sb1.toString());

            } else {
                log.info("当前订单 orderId【{}】,系统1审成功", order.getId());
                sys1checkSuccess(order.getId());
            }
        }
        log.info("结束 1审【微博任务】 taskId【{}】", task.getId());
    }


    //核对数据(admin页面调用的)
    @Override
    public void againCrawl(AgainCrawlVO vo) {
        Users users = userMapper.selectByUserName(vo.getUserName());

        //tasks
        List<UserTask> userTasks = taskMapper.selectByUserNameTime(users.getId(), vo.getStartTime(), vo.getEndTime());
        List<UserTask> newUserTasks = Lists.newArrayList();
        Set<Object> set = Sets.newHashSet();
        for (UserTask userTask : userTasks) {
            if (TaskStatusEnu.publishing.getValue() == userTask.getTaskStatus()) {
                log.info("taskId【{}】, 任务发布中，跳过..", userTask.getId());
                continue;
            }
            set.add(userTask.getId());
            newUserTasks.add(userTask);
        }
        log.info("任务集合:{}", set.toString());

        for (UserTask task : newUserTasks) {
            log.info("< ------------------ >");
            log.info("开始核对数据【微博任务】 taskId【{}】", task.getId());
            Set<String> accountSet = new HashSet<>();

            //orders
            List<UserOrder> orders = orderMapper.selectBytaskId(task.getId());
            List<UserOrder> newOrders = Lists.newArrayList();
            for (UserOrder order : orders) {
                if (OrderStatusEnu.sys_check_faild.getValue() != order.getOrderStatus()) {
                    log.info("当前订单 orderId【{}】不是1审失败, 跳过...", order.getId());
                    continue;
                }

                //获取所有的任务账号集合
                List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(order.getUserId(), task.getPlatform());
                for (UserPlatform userPlatform : userPlatforms) {
                    accountSet.add(userPlatform.getAccountUid());
                }
                newOrders.add(order);
            }

            //msgid
            String weiboContentMsgId = WeiboLinkUtil.getWeiboContentMsgId2(task.getSourceLink());
            //计算到期时间
            task.setCancelTime(DateUtils.calculateAgainCrawlCancelDatetime(task, vo));

            //爬取微博(转赞评)
            JSONArray taskForms = JSONObject.parseArray(task.getTaskform());
            for (Object taskForm : taskForms) {
                if (TaskPubEnu.comment.getValue() == (Integer) taskForm) {
                    log.info("开始爬取微博【评论】数据...");
                    weiboAuditUtil.weiboCommentData(task, weiboContentMsgId, CommonPythonCons.start_page, accountSet);
                }
                if (TaskPubEnu.like.getValue() == (Integer) taskForm) {
                    log.info("开始爬取微博【点赞】数据...");
                    weiboAuditUtil.weiboLikeData(task, weiboContentMsgId, CommonPythonCons.start_page, accountSet);
                }
                if (TaskPubEnu.forward.getValue() == (Integer) taskForm) {
                    log.info("开始爬取微博【转发】数据...");
                    weiboAuditUtil.weiboForwordData(task, weiboContentMsgId, CommonPythonCons.start_page, accountSet);
                }
            }

            //审核数据
            for (UserOrder order : newOrders) {
                //统计数据
                int commentTotalCount = 0;
                int likeTotalCount = 0;
                int forwardTotalCount = 0;
                List<UserPlatform> userPlatforms = userPlatformMapper.selectByUid(order.getUserId(), task.getPlatform());
                for (UserPlatform userPlatform : userPlatforms) {
                    commentTotalCount += commentMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid()).size();
                    likeTotalCount += likeMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid()).size();
                    forwardTotalCount += forwardMapper.selectByTaskIdAndUid(order.getTaskId(), userPlatform.getAccountUid()).size();
                }

                //对比数据是否正常
                log.info("开始核对数据 orderId为【{}】,订单数据", order.getId());
                Integer orderNum = order.getOrderNum();
                StringBuilder sb = new StringBuilder();
                List<Boolean> list = new ArrayList<>();

                if (taskForms.contains(TaskPubEnu.comment.getValue())) {
                    list.add(commentTotalCount >= orderNum);
                    sb.append(String.format(" 评论%s/%s", commentTotalCount, orderNum));
                }
                if (taskForms.contains(TaskPubEnu.like.getValue())) {
                    list.add(likeTotalCount >= orderNum);
                    sb.append(String.format(" 点赞%s/%s", likeTotalCount, orderNum));
                }
                if (taskForms.contains(TaskPubEnu.forward.getValue())) {
                    list.add(forwardTotalCount >= orderNum);
                    sb.append(String.format(" 转发%s/%s", forwardTotalCount, orderNum));
                }
                log.info("当前订单orderId【{}】，完成进度【{}】", order.getId(), sb);

                //结果
                if (list.contains(false)) {
                    log.info("当前订单orderId【{}】,人工核对数据 【失败】", order.getId());
                    StringBuilder sb1 = new StringBuilder();

                    if (taskForms.contains(TaskPubEnu.comment.getValue())) {
                        sb1.append(String.format(" 评论【%s/%s】 ", commentTotalCount, orderNum));
                    }
                    if (taskForms.contains(TaskPubEnu.like.getValue())) {
                        sb1.append(String.format(" 点赞【%s/%s】 ", likeTotalCount, orderNum));
                    }
                    if (taskForms.contains(TaskPubEnu.forward.getValue())) {
                        sb1.append(String.format(" 转发【%s/%s】 ", forwardTotalCount, orderNum));
                    }
                    artificialAuditFaild(order.getId(), sb1.toString());

                } else {
                    log.info("当前订单 orderId【{}】,人工核对数据【成功】", order.getId());
                    artificialAuditSuccess(order.getId());
                }
            }
            log.info("结束 核对数据【微博任务】 taskId【{}】", task.getId());
        }
    }


    //================================ xxxx==================================================
    //1审（成功）
    private void sys1checkSuccess(Integer orderId) {
        //order
        UserOrder order = orderMapper.selectByPrimaryKey(orderId);
        order.setCheckTime(new Date());
        order.setOrderStatus(OrderStatusEnu.finish.getValue());
        orderMapper.updateByPrimaryKeySelective(order);
        //order_check
        insertOrderCheck(orderId, OrderStatusEnu.finish, OrderCons.order_weibo_sys_1_ckeck_succeed);
        //pay
        insertPay(order);
    }

    //系统1审（失败）
    private void sys1CheckFaild(Integer orderId, String str) {
        //order
        UserOrder order = orderMapper.selectByPrimaryKey(orderId);
        order.setCheckTime(new Date());
        order.setOrderStatus(OrderStatusEnu.sys_check_faild.getValue());
        orderMapper.updateByPrimaryKeySelective(order);
        //order_check
        insertOrderCheck(orderId, OrderStatusEnu.sys_check_faild, str);
    }

    //人工核对数据（成功）
    private void artificialAuditSuccess(Integer orderId) {
        //order
        UserOrder order = orderMapper.selectByPrimaryKey(orderId);
        order.setCheckTime(new Date());
        order.setOrderStatus(OrderStatusEnu.finish.getValue());
        orderMapper.updateByPrimaryKeySelective(order);
        //order_check
        insertOrderCheck(orderId, OrderStatusEnu.artificial_audit_success, OrderCons.artificial_audit_success);
        //pay
        insertPay(order);
    }

    //人工核对数据（失败）
    private void artificialAuditFaild(Integer orderId, String str) {
        //order
        UserOrder order = orderMapper.selectByPrimaryKey(orderId);
        order.setCheckTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        //order_check
        insertOrderCheck(order.getId(), OrderStatusEnu.artificial_audit_faild, str);
    }

    //系统2审（失败）
    private void sys2CheckFaild(Integer orderId) {
        //order
        UserOrder order = orderMapper.selectByPrimaryKey(orderId);
        order.setOrderStatus(OrderStatusEnu.sys_2_check_faild.getValue());
        orderMapper.updateByPrimaryKeySelective(order);
        //order_check
        insertOrderCheck(orderId, OrderStatusEnu.sys_2_check_faild, OrderCons.order_weibo_sys_2_ckeck_failure);
    }

    //系统2审(成功)
    private void sys2CheckSuccess(Integer orderId) {
        insertOrderCheck(orderId, OrderStatusEnu.sys_2_check_success, OrderCons.order_weibo_sys_2_ckeck_succeed);
    }

    //系统2审(跳过)
    private void sys2CheckSkip(Integer orderId, String str) {
        insertOrderCheck(orderId, OrderStatusEnu.sys_2_check_skip, str);
    }

    //order_check
    private void insertOrderCheck(Integer orderId, OrderStatusEnu enu, String str) {
        UserOrderCheck userOrderCheck = new UserOrderCheck();
        userOrderCheck.setOrderId(orderId);
        userOrderCheck.setCheckStatus(enu.getValue());
        userOrderCheck.setCheckReason(str);
        userOrderCheck.setCreateTime(new Date());
        orderCheckMapper.insertSelective(userOrderCheck);
    }

    //pay
    private void insertPay(UserOrder order) {
        UserPay userPay = payMapper.selectByOrderId(order.getId());
        if (userPay == null) {
            UserPay pay = new UserPay();
            pay.setOrderId(order.getId());
            pay.setMoney(new BigDecimal(order.getOrderNum()).multiply(BigDecimal.valueOf(order.getOrderPrice())).doubleValue());
            pay.setPayStatus(OrderPayStatusEnu.no_balance.getValue());
            pay.setCreateTime(new Date());
            pay.setPayNo(MathUtils.payNum());
            payMapper.insert(pay);
        } else {
            //冻结的话解除
            if (OrderPayStatusEnu.freeze.getValue() == userPay.getPayStatus()) {
                userPay.setPayStatus(OrderPayStatusEnu.no_balance.getValue());
                payMapper.updateByPrimaryKeySelective(userPay);
            }
        }
    }

}
