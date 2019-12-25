package com.loushi.mapper;

import com.loushi.model.UserPay;
import com.loushi.vo.order.CommissionListVO;
import com.loushi.vo.order.RemitRecordListVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface UserPayMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserPay record);

    int insertSelective(UserPay record);

    UserPay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPay record);

    int updateByPrimaryKey(UserPay record);


    List<RemitRecordListVO> remitRecordList(@Param("uid") Integer uid,
                                            @Param("orderPayStatus") Integer orderPayStatus);


    List<CommissionListVO> selectByUidTime(@Param("uid") Integer uid,
                                           @Param("filterTime") String filterTime);


    UserPay selectByOrderId(Integer orderId);

}