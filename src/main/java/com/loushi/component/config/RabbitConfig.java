package com.loushi.component.config;

import com.loushi.component.contants.RabbitMqCons;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 队列配置文件
 */

@Configuration
@EnableRabbit
public class RabbitConfig {

    //================================================ 1审延迟队列 ============================================
    @Bean
    public DirectExchange oneSysAuditDelayTimeExchange() {
        return new DirectExchange(RabbitMqCons.one_sys_audit_delay_time_exchange);
    }

    @Bean
    public Queue oneSysAuditDelayTimeQueue() {
        return new Queue(RabbitMqCons.one_sys_audit_delay_time_queue_name);
    }

    @Bean
    public Binding oneSysAuditDelayTimeBinding() {
        return BindingBuilder.bind(oneSysAuditDelayTimeQueue()).to(oneSysAuditDelayTimeExchange()).with(RabbitMqCons.one_sys_audit_delay_time_queue_name);
    }

    @Bean
    public Queue oneSysAuditDelayTimeDeadLetterQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", RabbitMqCons.one_sys_audit_delay_time_exchange);
        arguments.put("x-dead-letter-routing-key", RabbitMqCons.one_sys_audit_delay_time_queue_name);
        return new Queue(RabbitMqCons.one_sys_audit_delay_time_dead_letter_queue_name, true, false, false, arguments);
    }

    @Bean
    public Binding oneSysAuditDelayTimeDeadLetterBinding() {
        return BindingBuilder.bind(oneSysAuditDelayTimeDeadLetterQueue()).to(oneSysAuditDelayTimeExchange()).with(RabbitMqCons.one_sys_audit_delay_time_dead_letter_queue_name);
    }


    //================================================任务发布延迟队列============================================
    //信道配置
    @Bean
    public DirectExchange publishTaskExchange() {
        return new DirectExchange(RabbitMqCons.publish_task_exchange);
    }

    //发布任务(监听队列)
    @Bean
    public Queue publishTaskQueue() {
        return new Queue(RabbitMqCons.publish_task_queue_name);
    }

    @Bean
    public Binding publishTaskBinding() {
        return BindingBuilder.bind(publishTaskQueue()).to(publishTaskExchange()).with(RabbitMqCons.publish_task_queue_name);
    }


    //发布死信队列0
    @Bean
    public Queue publishTaskDeadLetterQueue0() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", RabbitMqCons.publish_task_exchange);
        arguments.put("x-dead-letter-routing-key", RabbitMqCons.publish_task_queue_name);
        return new Queue(RabbitMqCons.publish_task_dead_letter_queue_name_1, true, false, false, arguments);
    }

    @Bean
    public Binding publishTaskDeadLetterBinding0() {
        return BindingBuilder.bind(publishTaskDeadLetterQueue0()).to(publishTaskExchange()).with(RabbitMqCons.publish_task_dead_letter_queue_name_1);
    }

    //发布死信队列1
    @Bean
    public Queue publishTaskDeadLetterQueue1() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", RabbitMqCons.publish_task_exchange);
        arguments.put("x-dead-letter-routing-key", RabbitMqCons.publish_task_queue_name);
        return new Queue(RabbitMqCons.publish_task_dead_letter_queue_name_5, true, false, false, arguments);
    }

    @Bean
    public Binding publishTaskDeadLetterBinding1() {
        return BindingBuilder.bind(publishTaskDeadLetterQueue1()).to(publishTaskExchange()).with(RabbitMqCons.publish_task_dead_letter_queue_name_5);
    }

    //发布死信队列2
    @Bean
    public Queue publishTaskDeadLetterQueue2() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", RabbitMqCons.publish_task_exchange);
        arguments.put("x-dead-letter-routing-key", RabbitMqCons.publish_task_queue_name);
        return new Queue(RabbitMqCons.publish_task_dead_letter_queue_name_10, true, false, false, arguments);
    }

    @Bean
    public Binding publishTaskDeadLetterBinding2() {
        return BindingBuilder.bind(publishTaskDeadLetterQueue2()).to(publishTaskExchange()).with(RabbitMqCons.publish_task_dead_letter_queue_name_10);
    }

    //发布死信队列3
    @Bean
    public Queue publishTaskDeadLetterQueue3() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", RabbitMqCons.publish_task_exchange);
        arguments.put("x-dead-letter-routing-key", RabbitMqCons.publish_task_queue_name);
        return new Queue(RabbitMqCons.publish_task_dead_letter_queue_name_15, true, false, false, arguments);
    }

    @Bean
    public Binding publishTaskDeadLetterBinding3() {
        return BindingBuilder.bind(publishTaskDeadLetterQueue3()).to(publishTaskExchange()).with(RabbitMqCons.publish_task_dead_letter_queue_name_15);
    }

    //发布死信队列4
    @Bean
    public Queue publishTaskDeadLetterQueue4() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", RabbitMqCons.publish_task_exchange);
        arguments.put("x-dead-letter-routing-key", RabbitMqCons.publish_task_queue_name);
        return new Queue(RabbitMqCons.publish_task_dead_letter_queue_name_30, true, false, false, arguments);
    }

    @Bean
    public Binding publishTaskDeadLetterBinding4() {
        return BindingBuilder.bind(publishTaskDeadLetterQueue4()).to(publishTaskExchange()).with(RabbitMqCons.publish_task_dead_letter_queue_name_30);
    }


}

