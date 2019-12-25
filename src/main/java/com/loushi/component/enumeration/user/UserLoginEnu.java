package com.loushi.component.enumeration.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户状态
 * @author 技术部
 */

public enum UserLoginEnu {

    /**
     * 授权成功
     */
    authorization(1, "授权成功"),
    /**
     * 未登入
     */
    no_login(2, "未登入"),

    /**
     * 已登入(用户)
     */
    user_yes_login(3, "用户已登入"),
    /**
     * 已登入(博主)
     */
    bozhu_yes_login(4, "博主已登入");


    @Setter
    @Getter
    private int value;
    @Setter
    @Getter
    private String name;

    private UserLoginEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getByValue(int value) {
        for (UserLoginEnu status : values()) {
            if (status.getValue() == value) {
                return status.getName();
            }
        }
        return null;
    }

}
