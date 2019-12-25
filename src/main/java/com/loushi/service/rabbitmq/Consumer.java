package com.loushi.service.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.loushi.component.contants.RabbitMqCons;
import com.loushi.service.ITaskService;
import com.loushi.service.weibo.IWeiboCheck;
import com.loushi.vo.task.PublishTaskRabbitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消费者
 * @author 技术部
 */

@Component
@Slf4j
public class Consumer {

    @Resource
    private ITaskService taskService;
    @Resource
    private IWeiboCheck weiboCheck;


    /**
     * (消费)发布任务
     */
    @RabbitListener(queues = RabbitMqCons.publish_task_queue_name)
    public void publishTaskConsumer(String msg) {
        try {
            log.info("开始消费【队列】---> 发布任务,接收到的数据... 【{}】", msg);
            PublishTaskRabbitVO vo = JSONObject.parseObject(msg, PublishTaskRabbitVO.class);
            taskService.publishTaskConsumer(vo);
            log.info("结束消费【队列】...【{}】", msg);

        } catch (Exception e) {
            log.error("任务，消费异常...", e);
        }
    }

    /**
     * (消费)1审核延迟
     */
    @RabbitListener(queues = RabbitMqCons.one_sys_audit_delay_time_queue_name)
    public void oneSysAuditDelayTimeConsumer(String msg) {
        try {
            log.info("开始消费【队列】---> 1审核延迟,接收到的数据... 【{}】", msg);
            PublishTaskRabbitVO vo = JSONObject.parseObject(msg, PublishTaskRabbitVO.class);
            weiboCheck.firstAuditWeiboTask(vo);
            log.info("结束消费【队列】...【{}】", msg);

        } catch (Exception e) {
            log.error("任务，消费异常...", e);
        }
    }

}
