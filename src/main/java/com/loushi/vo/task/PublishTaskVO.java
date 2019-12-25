package com.loushi.vo.task;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PublishTaskVO {

    private String desc;


    @NotNull(message = "reserveDay不能为空")
    private Integer reserveDay;

    /**
     * 平台类型
     */
    @NotNull(message = "platform不能为空")
    private Integer platform;

    /**
     * 任务类型
     */
    @NotNull(message = "taskform不能为空")
    private List<Integer> taskform;

    /**
     * 对应文章链接
     */
    @NotNull(message = "sourceLink不能为空")
    private String sourceLink;

    /**
     * 任务单价
     */
    @NotNull(message = "price不能为空")
    private String price;
    /**
     * 任务数量
     */
    @NotNull(message = "totalNum不能为空")
    private Integer totalNum;

    /**
     * 任务结束时间(多少分钟后结束)
     */
    @NotNull(message = "consumeTime不能为空")
    private Integer consumeTime;

}

