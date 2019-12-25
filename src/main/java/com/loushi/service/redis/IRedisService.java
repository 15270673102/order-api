package com.loushi.service.redis;

import com.loushi.model.UserTask;
import com.loushi.vo.task.PublishTaskVO;

public interface IRedisService {

    //发布保留时间redis监听
    void publishTask(UserTask task, PublishTaskVO vo);

}
