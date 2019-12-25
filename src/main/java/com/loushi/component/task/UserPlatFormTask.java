package com.loushi.component.task;

import com.alibaba.fastjson.JSONObject;
import com.loushi.mapper.UserPlatformMapper;
import com.loushi.model.UserPlatform;
import com.loushi.utils.commonPython.PythonHttpApi;
import com.loushi.vo.commonPython.WeiboUserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
@Slf4j
public class UserPlatFormTask {

    @Resource
    private UserPlatformMapper userPlatformMapper;
    @Resource
    private PythonHttpApi pythonHttpApi;


    @Scheduled(cron = "0 0 1 * * ?")
    public void updateWeiboAccountNickName() {
        log.info("定时任务 --> 开始【检查绑定的昵称是否改变】");

        List<UserPlatform> userPlatforms = userPlatformMapper.selectAll();
        for (UserPlatform userPlatform : userPlatforms) {
            Object data = pythonHttpApi.getWeiboUserInfo(userPlatform.getAccountUid()).getData();
            if (data == null) {
                log.error("更新微博账号昵称异常...{} ...data:{}", userPlatform, data);
                continue;
            }

            WeiboUserInfoVO weiboUserInfoVO = JSONObject.parseObject(data.toString(), WeiboUserInfoVO.class);
            if (userPlatform.getAccountNickName().equals(weiboUserInfoVO.getScreen_name())) {
                continue;
            }

            userPlatform.setAccountNickName(weiboUserInfoVO.getScreen_name());
            userPlatformMapper.updateByPrimaryKeySelective(userPlatform);
            log.info("更新【微博平台】昵称... 【{}】--> 【{}】", userPlatform.getAccountNickName(), weiboUserInfoVO.getScreen_name());
        }
        log.info("任务结束了");
    }

}
