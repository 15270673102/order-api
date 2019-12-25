package com.loushi.component.enumeration.task;

import com.loushi.vo.util.EnuKeyValueModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布任务过期时间
 * @author 技术部
 */

public enum TaskPubConsumeTimeEnu {

    one(1, "1"),
    five(5, "5"),
    ten(10, "10"),
    fifteen(15, "15"),
    thirty(30, "30");

    @Setter
    @Getter
    private int value;
    @Setter
    @Getter
    private String name;

    private TaskPubConsumeTimeEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static List<EnuKeyValueModel> getTaskPubConsumeTimeEnu(boolean isProd) {
        List<EnuKeyValueModel> list = new ArrayList<>();
        for (TaskPubConsumeTimeEnu taskPubConsumeTimeEnu : values()) {
            if (isProd && taskPubConsumeTimeEnu.getValue() == TaskPubConsumeTimeEnu.one.getValue())
                continue;
            list.add(new EnuKeyValueModel(taskPubConsumeTimeEnu.getValue(), taskPubConsumeTimeEnu.getName()));
        }
        return list;
    }


    public static void main(String[] args) {
        System.out.println(getTaskPubConsumeTimeEnu(true));
    }
}
