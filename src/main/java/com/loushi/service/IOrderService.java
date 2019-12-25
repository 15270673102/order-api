package com.loushi.service;

import com.github.pagehelper.PageInfo;
import com.loushi.model.UserAppeal;
import com.loushi.model.UserOrder;
import com.loushi.vo.order.*;
import com.loushi.vo.user.CommissionRecordVO;

import java.util.List;

/**
 * 订单表
 */

public interface IOrderService {


    /**
     * 接单操作
     * @param uid
     * @param taskId
     * @param num
     * @return
     */
    JoinOrderVO joinOrder(Integer uid, Integer taskId, Integer num) throws Exception;

    /**
     * 我的订单
     * @param uid
     * @return
     */
    PageInfo<UserOrder> userOrderList(Integer uid, UserOrderVO vo);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    UserOrder orderItem(Integer orderId);

    /**
     * 佣金任务列表
     * @param uid
     * @param pageNum
     * @param pageSize
     * @param filterTime
     * @return
     */
    PageInfo<CommissionListVO> commissionList(Integer uid, Integer pageNum, Integer pageSize, String filterTime);

    /**
     * 佣金记录
     * @param uid
     * @return
     */
    CommissionRecordVO commissionRecord(Integer uid);

    /**
     * 保存订单失败申述
     * @param uid
     */
    void saveOrderCheckFaild(Integer uid, SaveOrderCheckFaildVO vo) throws Exception;


    /**
     *打款记录列表
     * @param uid
     * @return
     */
    List<RemitRecordListVO> remitRecordList(Integer uid);

    /**
     * 订单协商列表
     * @param orderId
     */
    List<UserAppeal> appealList(Integer orderId);

}
