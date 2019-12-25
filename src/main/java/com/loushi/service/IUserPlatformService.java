package com.loushi.service;

import com.github.pagehelper.PageInfo;
import com.loushi.model.UserPlatform;
import com.loushi.vo.user.SaveUserPlatformDataVO;

/**
 * 用户平台表
 */

public interface IUserPlatformService {


    /**
     * 用户平台资料
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserPlatform> userPlatformData(Integer uid, Integer pageNum, Integer pageSize);


    /**
     * 保存用户平台资料
     * @param uid
     */
    void saveUserPlatformData(Integer uid, SaveUserPlatformDataVO vo) throws Exception;

    /**
     * 删除用户平台资料
     * @param userPlatformId
     */
    void deleteUserPlatformData(Integer userPlatformId);
}
