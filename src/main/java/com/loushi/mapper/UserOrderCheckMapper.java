package com.loushi.mapper;

import com.loushi.model.UserOrderCheck;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;
import java.util.List;

public interface UserOrderCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOrderCheck record);

    int insertSelective(UserOrderCheck record);

    UserOrderCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrderCheck record);

    int updateByPrimaryKey(UserOrderCheck record);

    Date selectByMaxCheckTime(Integer orderId);

    List<UserOrderCheck> selectByOrderIdStatus(@Param("orderId") Integer orderId,
                                               @Param("orderStatus1") Integer orderStatus1,
                                               @Param("orderStatus2") Integer orderStatus2,
                                               @Param("orderStatus3") Integer orderStatus3
    );
}