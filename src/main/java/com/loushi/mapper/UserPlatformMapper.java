package com.loushi.mapper;

import com.loushi.model.UserPlatform;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface UserPlatformMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserPlatform record);

    int insertSelective(UserPlatform record);

    UserPlatform selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPlatform record);

    int updateByPrimaryKey(UserPlatform record);

    List<UserPlatform> selectAll();

    /**
     * 用户平台资料
     * @param uid
     */
    List<UserPlatform> selectByUid(
            @Param("uid") Integer uid,
            @Param("platformTpye") Integer platformTpye);

    /**
     * 获取这个uid是不是已经绑定了
     * @param userPlatformType
     * @param accountId
     */
    int selectByUidPlatFormAccountUid(
            @Param("userPlatformType") Integer userPlatformType,
            @Param("accountId") String accountId);

    /**
     * 获取平台资料
     * @param uid
     * @param userPlatformType
     */
    int selectByUidPlatFormType(
            @Param("uid") Integer uid,
            @Param("userPlatformType") Integer userPlatformType);


    List<UserPlatform> selectByUserId(Integer userId);

}