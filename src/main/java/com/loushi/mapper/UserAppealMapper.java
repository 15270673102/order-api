package com.loushi.mapper;

import com.loushi.model.UserAppeal;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;
import java.util.List;

public interface UserAppealMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserAppeal record);

    int insertSelective(UserAppeal record);

    UserAppeal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAppeal record);

    int updateByPrimaryKey(UserAppeal record);

    /**
     * 订单协商列表
     */
    List<UserAppeal> appealList(Integer orderId);

    /**
     * 最新申述时间
     */
    Date selectByOrderIdMaxCreateTime(@Param("orderId") Integer orderId,
                                      @Param("value") Integer value);
}