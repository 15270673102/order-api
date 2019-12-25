package com.loushi.mapper;

import com.loushi.model.Users;
import io.lettuce.core.dynamic.annotation.Param;

public interface UsersMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    /**
     * 登入
     */
    Users selectByUsernamePassword(
            @Param("username") String username,
            @Param("password") String password,
            @Param("userRole") Integer userRole);

    /**
     * 根据手机号码获取用户
     */
    Users selectByPhone(
            @Param("phoneNum") String phoneNum,
            @Param("userRoleType") Integer userRoleType);

    Users selectByUserName(String userName);

}