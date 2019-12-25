package com.loushi.component.enumeration.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户表用户状态类型【1：已注册】【2：已绑定】【3：已锁定】
 * @author 技术部
 */

public enum UserStatusEnu {

    /**
     * 已注册
     */
    register(1, "已注册"),
    /**
     * 已绑定
     */
    bing(2, "已绑定"),
    /**
     * 已锁定
     */
    lock(3, "已锁定");

    @Setter
    @Getter
    private int value;
    @Setter
    @Getter
    private String name;

    private UserStatusEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getByValue(int value) {
        for (UserStatusEnu status : values()) {
            if (status.getValue() == value) {
                return status.getName();
            }
        }
        return null;
    }

}
