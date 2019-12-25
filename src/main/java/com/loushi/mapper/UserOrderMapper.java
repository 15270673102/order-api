package com.loushi.mapper;

import com.loushi.model.UserOrder;
import com.loushi.vo.order.CommissionRecordModelVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/*
 * 订单表
 * @author 技术部
 */

public interface UserOrderMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserOrder record);

    int insertSelective(UserOrder record);

    UserOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrder record);

    int updateByPrimaryKey(UserOrder record);

    /**
     * 我的订单列表
     */
    List<UserOrder> selectByStateUid(@Param("uid") Integer uid,
                                     @Param("state") Integer state);

    /**
     * 我的订单列表
     */
    List<UserOrder> selectByCheckFaild(
            @Param("uid") Integer uid,
            @Param("state1") Integer state1,
            @Param("state2") Integer state2,
            @Param("state3") Integer state3,
            @Param("state4") Integer state4);

    /**
     * 佣金任务列表
     */
    List<UserOrder> selectByUidTime(
            @Param("uid") Integer uid,
            @Param("filterTime") String filterTime,
            @Param("orderStatus") Integer orderStatus);

    /**
     * 佣金记录
     */
    List<CommissionRecordModelVO> selectByUid(Integer uid);

    /**
     * 保存订单失败申述
     */
    UserOrder selectByUidOrderId(@Param("uid") Integer uid, @Param("orderId") Integer orderId);

    /**
     * 根据任务id，获取订单集合
     */
    List<UserOrder> selectBytaskId(Integer taskId);


    /**
     * 判断这个任务是不是接过了
     */
    int selectByUidTaskId(@Param("uid") Integer uid,
                          @Param("taskId") Integer taskId);

}