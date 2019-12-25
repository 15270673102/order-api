package com.loushi.component.enumeration.task;

import com.loushi.vo.util.EnuKeyValueModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户平台类型
 * @author 技术部
 */
@Getter
public enum TaskPlatformEnu {

    weibo(1, "微博");


    private int value;
    private String name;

    private TaskPlatformEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getByValue(int value) {
        for (TaskPlatformEnu status : values()) {
            if (status.getValue() == value) {
                return status.getName();
            }
        }
        return null;
    }

    public static List<EnuKeyValueModel> getTaskPlatformEnu() {
        List<EnuKeyValueModel> list = new ArrayList<>();
        for (TaskPlatformEnu userPlatEnu : values()) {
            list.add(new EnuKeyValueModel(userPlatEnu.getValue(), userPlatEnu.getName()));
        }
        return list;
    }

}
