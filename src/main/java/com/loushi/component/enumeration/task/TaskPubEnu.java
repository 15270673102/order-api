package com.loushi.component.enumeration.task;

import com.loushi.vo.util.EnuKeyValueModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务类型
 * @author 技术部
 */

@Getter
public enum TaskPubEnu {

    comment(1, "评论"),
    like(2, "点赞"),
    forward(3, "转发");

    private int value;
    private String name;

    private TaskPubEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static List<EnuKeyValueModel> getTaskPbuEnu() {
        List<EnuKeyValueModel> list = new ArrayList<>();
        for (TaskPubEnu taskPubEnu : values()) {
            list.add(new EnuKeyValueModel(taskPubEnu.getValue(), taskPubEnu.getName()));
        }
        return list;
    }

}
