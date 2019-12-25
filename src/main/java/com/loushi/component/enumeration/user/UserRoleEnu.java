package com.loushi.component.enumeration.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户角色类型
 * @author 技术部
 */

public enum UserRoleEnu {

    /**
     * 接单角色
     */
    user(1, "user"),
    /**
     * 博主
     */
    bozhu(2, "bozhu");


    @Setter
    @Getter
    private int value;
    @Setter
    @Getter
    private String name;

    private UserRoleEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getByValue(int value) {
        for (UserRoleEnu status : values()) {
            if (status.getValue() == value) {
                return status.getName();
            }
        }
        return null;
    }

}
