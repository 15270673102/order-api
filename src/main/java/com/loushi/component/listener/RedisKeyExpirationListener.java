package com.loushi.component.listener;

import cn.hutool.core.util.ReUtil;
import com.loushi.component.contants.PublishTaskCons;
import com.loushi.mapper.UserOrderMapper;
import com.loushi.service.IOrderService;
import com.loushi.service.weibo.IWeiboCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 监控redis key过期时间
 */

@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Resource
    private IWeiboCheck weiboCheck;


    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    private static Pattern matcherRetainTimeKey = Pattern.compile(".*:(.*)");

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        //案例 order_api:publishTaskRedisPrefix:weibo:123  后面的数字是task表id
        String expiredKey = message.toString();

        if (expiredKey.startsWith(PublishTaskCons.weibo)) {
            if (ReUtil.contains(matcherRetainTimeKey, expiredKey)) {
                try {
                    String taskId = ReUtil.getGroup1(matcherRetainTimeKey, expiredKey);
                    weiboCheck.secondAuditWeiboTask(Integer.valueOf(taskId));

                } catch (Exception e) {
                    log.error("redis监听异常...", e);
                }
            }
        }
    }

}
