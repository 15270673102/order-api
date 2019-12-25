package com.loushi.component.enumeration.task;

import lombok.Getter;
import lombok.Setter;

/**
 * 任务状态
 * @author 技术部
 */

@Getter
public enum TaskJoinEnu {

    able_join(1, "能接单"),
    no_join(2, "不能接单");

    private int value;
    private String name;

    private TaskJoinEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
