package com.loushi.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loushi.component.enumeration.task.TaskPlatformEnu;
import com.loushi.component.properties.BasisProperties;
import com.loushi.mapper.UserPlatformMapper;
import com.loushi.model.UserPlatform;
import com.loushi.utils.commonPython.PythonHttpApi;
import com.loushi.vo.commonPython.WeiboUserInfoVO;
import com.loushi.vo.user.SaveUserPlatformDataVO;
import lombok.Synchronized;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户平台表
 */

@Service
@Transactional(noRollbackFor = Exception.class)
public class UserPlatformServiceImpl implements IUserPlatformService {

    @Resource
    private UserPlatformMapper userPlatformMapper;
    @Resource
    private PythonHttpApi pythonHttpApi;
    @Resource
    private BasisProperties basisProperties;


    @Override
    public PageInfo<UserPlatform> userPlatformData(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserPlatform> list = userPlatformMapper.selectByUid(uid, null);
        for (UserPlatform userPlatform : list) {
            userPlatform.setPlatformTypeStr(TaskPlatformEnu.getByValue(userPlatform.getPlatformType()));
        }
        return new PageInfo<>(list);
    }

    @Override
    @Synchronized
    public void saveUserPlatformData(Integer uid, SaveUserPlatformDataVO vo) throws Exception {
        int accounts = userPlatformMapper.selectByUidPlatFormType(uid, vo.getUserPlatformType());
        if (accounts >= basisProperties.getMaxPlatFormNum())
            throw new Exception(String.format("最多绑定%d个微博账号...", basisProperties.getMaxPlatFormNum()));

        //获取这个uid是不是已经绑定了
        int count = userPlatformMapper.selectByUidPlatFormAccountUid(vo.getUserPlatformType(), vo.getUID());
        if (count > 0)
            throw new Exception(String.format("该【%s】账号【%s】，已经绑定过了...", TaskPlatformEnu.getByValue(vo.getUserPlatformType()), vo.getUID()));

        //微博平台
        if (vo.getUserPlatformType() == TaskPlatformEnu.weibo.getValue()) {
            Object data = pythonHttpApi.getWeiboUserInfo(vo.getUID()).getData();
            if (data == null)
                throw new Exception("uid有误,请确认...");

            WeiboUserInfoVO weiboUserInfoVO = JSONObject.parseObject(data.toString(), WeiboUserInfoVO.class);
            if (!weiboUserInfoVO.getScreen_name().equals(vo.getNickName()))
                throw new Exception("昵称有误,请确认...");
        }

        UserPlatform userPlatform = new UserPlatform();
        userPlatform.setUserId(uid);
        userPlatform.setPlatformType(vo.getUserPlatformType());
        userPlatform.setAccountNickName(vo.getNickName());
        userPlatform.setAccountUid(vo.getUID());
        userPlatform.setCreateTime(new Date());
        userPlatformMapper.insertSelective(userPlatform);
    }

    @Override
    public void deleteUserPlatformData(Integer userPlatformId) {
        userPlatformMapper.deleteByPrimaryKey(userPlatformId);
    }

}
