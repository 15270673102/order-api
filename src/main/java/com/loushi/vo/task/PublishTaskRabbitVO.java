package com.loushi.vo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishTaskRabbitVO {

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 任务id
     */
    private Integer taskId;

    private String msg;
}
