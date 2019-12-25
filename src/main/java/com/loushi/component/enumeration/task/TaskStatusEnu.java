package com.loushi.component.enumeration.task;

import lombok.Getter;
import lombok.Setter;

/**
 * 任务状态
 * @author 技术部
 */

@Getter
public enum TaskStatusEnu {

    all(0, "全部"),

    publishing(1, "发布中"),
    complete(2, "已完成(接完单了)"),
    finish(4, "已结束(到时间了)"),
    cancel(3, "博主撤单");


    private int value;
    private String name;

    private TaskStatusEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
