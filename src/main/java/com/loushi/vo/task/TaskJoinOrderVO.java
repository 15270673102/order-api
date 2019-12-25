package com.loushi.vo.task;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TaskJoinOrderVO {

    /**
     * 任务id
     */
    @NotNull(message = "taskId不能为空")
    private Integer taskId;

    /**
     * 接单数量
     */
    @NotNull(message = "num不能为空")
    private Integer num;
}
