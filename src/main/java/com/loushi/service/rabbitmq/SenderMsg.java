package com.loushi.service.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.loushi.component.contants.RabbitMqCons;
import com.loushi.component.enumeration.task.TaskPubConsumeTimeEnu;
import com.loushi.component.properties.BasisProperties;
import com.loushi.model.UserTask;
import com.loushi.vo.task.PublishTaskRabbitVO;
import com.loushi.vo.task.PublishTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * rabbit生成者
 */

@Component
@Slf4j
public class SenderMsg {

    @Resource
    private AmqpTemplate rabbitTemplate;
    @Resource
    private BasisProperties basisProperties;

    private static final String PUBLISH_TASK_MSG = "博主-->发任务";

    private static final String ONE_SYS_AUDIT_DELAY_TIME_MSG = "1审-->延迟";


    /**
     * 微博发消息
     */
    public void publishTask(String msgId, UserTask task, PublishTaskVO vo) throws Exception {
        //消息队列rabbitmq
        Integer consumeTime = vo.getConsumeTime();
        String routingKey = "";
        if (consumeTime == TaskPubConsumeTimeEnu.five.getValue())
            routingKey = RabbitMqCons.publish_task_dead_letter_queue_name_5;

        else if (consumeTime == TaskPubConsumeTimeEnu.ten.getValue())
            routingKey = RabbitMqCons.publish_task_dead_letter_queue_name_10;

        else if (consumeTime == TaskPubConsumeTimeEnu.fifteen.getValue())
            routingKey = RabbitMqCons.publish_task_dead_letter_queue_name_15;

        else if (consumeTime == TaskPubConsumeTimeEnu.thirty.getValue())
            routingKey = RabbitMqCons.publish_task_dead_letter_queue_name_30;

        else if (consumeTime == TaskPubConsumeTimeEnu.one.getValue())
            routingKey = RabbitMqCons.publish_task_dead_letter_queue_name_1;


        //发消息到延迟队列中
        String exchange = RabbitMqCons.publish_task_exchange;
        String times = String.valueOf(consumeTime * 60 * 1000);
        PublishTaskRabbitVO publishTaskRabbitVO = new PublishTaskRabbitVO(msgId, task.getId(), PUBLISH_TASK_MSG);

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, JSONObject.toJSONString(publishTaskRabbitVO), message -> {
                message.getMessageProperties().setExpiration(times);
                return message;
            });
            log.info("【队列】::: 发布任务【{}】, 中途睡眠【{}】分钟, amq发消息...{}", routingKey, consumeTime, publishTaskRabbitVO);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("任务发布异常");
        }
    }


    /**
     * (1审延迟时间)队列
     * @param vo
     */
    public void oneSysAuditDelayTime(PublishTaskRabbitVO vo) {

        String exchange = RabbitMqCons.one_sys_audit_delay_time_exchange;
        String times = String.valueOf(basisProperties.getOneSysAuditDelaySecond() * 1000);
        String routingKey = RabbitMqCons.one_sys_audit_delay_time_dead_letter_queue_name;
        vo.setMsg(ONE_SYS_AUDIT_DELAY_TIME_MSG);

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, JSONObject.toJSONString(vo), message -> {
                message.getMessageProperties().setExpiration(times);
                return message;
            });
            log.info("【队列】::: 1审延迟时间【{}】, 中途睡眠【{}】分钟, amq发消息...{}", routingKey, basisProperties.getOneSysAuditDelaySecond() / 60, vo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
