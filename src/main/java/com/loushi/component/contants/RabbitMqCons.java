package com.loushi.component.contants;

/**
 * rabbitmq
 */

public class RabbitMqCons extends CommonCons {

    /**
     * rabbitMq基本路径
     */
    private static final String rabbitmq_base_url = "order.api.";


    //==========================    这个是发任务的队列     ====================================
    //exchange name
    public static final String publish_task_exchange = rabbitmq_base_url + "publish.task.exchange";

    //DLX repeat QUEUE 死信转发队列
    public static final String publish_task_queue_name = rabbitmq_base_url + "publish.task.queue";

    //DLX QUEUE0
    public static final String publish_task_dead_letter_queue_name_1 = rabbitmq_base_url + "publish.task.dead.letter.queue.1";
    //DLX QUEUE1
    public static final String publish_task_dead_letter_queue_name_5 = rabbitmq_base_url + "publish.task.dead.letter.queue.5";
    //DLX QUEUE2
    public static final String publish_task_dead_letter_queue_name_10 = rabbitmq_base_url + "publish.task.dead.letter.queue.10";
    //DLX QUEUE3
    public static final String publish_task_dead_letter_queue_name_15 = rabbitmq_base_url + "publish.task.dead.letter.queue.15";
    //DLX QUEUE4
    public static final String publish_task_dead_letter_queue_name_30 = rabbitmq_base_url + "publish.task.dead.letter.queue.30";


    //==========================    1审延迟队列    ====================================
    //exchange name
    public static final String one_sys_audit_delay_time_exchange = rabbitmq_base_url + "one_sys_audit_delay_time.exchange";

    //DLX repeat QUEUE 死信转发队列
    public static final String one_sys_audit_delay_time_queue_name = rabbitmq_base_url + "one_sys_audit_delay_time.queue";

    //DLX QUEUE0
    public static final String one_sys_audit_delay_time_dead_letter_queue_name = rabbitmq_base_url + "one_sys_audit_delay_time.dead.letter.queue";

}
