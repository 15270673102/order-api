package com.loushi.service.redis;


import com.loushi.component.contants.PublishTaskCons;
import com.loushi.component.properties.BasisProperties;
import com.loushi.controller.common.AbstractProfiles;
import com.loushi.model.UserTask;
import com.loushi.utils.RedisUtil;
import com.loushi.vo.task.PublishTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RedisServiceImpl extends AbstractProfiles implements IRedisService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private BasisProperties basisProperties;


    //发布保留时间redis监听
    @Override
    public void publishTask(UserTask task, PublishTaskVO vo) {
        //案例 order_api:publishTaskRedisPrefix:weibo:123  -->  后面的数字是task表id
        String redisKey = PublishTaskCons.weibo + task.getId();
        long saveTime = vo.getReserveDay() * 24 * 60 * 60;

        if (!getActive().equals(basisProperties.getPro())) {
            saveTime = (vo.getConsumeTime() + 5) * 60;
            log.info("当前开发环境...【{}】 保留时间减低为:{}分钟, 方便测试...", getActive(), saveTime / 60);
        } else {
            log.info("发任务【{}】,保留时间【{}】天", task.getId(), saveTime);
        }
        redisUtil.set(redisKey, "", saveTime);
    }

}
