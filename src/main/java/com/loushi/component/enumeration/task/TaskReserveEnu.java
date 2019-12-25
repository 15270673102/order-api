package com.loushi.component.enumeration.task;

import com.loushi.vo.util.EnuKeyValueModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 过期时间
 * @author 技术部
 */

public enum TaskReserveEnu {

    one(1, "1"),
    two(2, "2"),
    three(3, "3"),
    four(4, "4"),
    five(5, "5"),
    six(6, "6"),
    seven(7, "7"),
    eight(8, "8"),
    nine(9, "9"),
    ten(10, "10");


    @Setter
    @Getter
    private int value;
    @Setter
    @Getter
    private String name;

    private TaskReserveEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static List<EnuKeyValueModel> getReserveEnu() {
        List<EnuKeyValueModel> list = new ArrayList<>();
        for (TaskReserveEnu taskReserveEnu : values()) {
            list.add(new EnuKeyValueModel(taskReserveEnu.getValue(), taskReserveEnu.getName()));
        }
        return list;
    }

}
