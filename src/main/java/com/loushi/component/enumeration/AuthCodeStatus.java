package com.loushi.component.enumeration;

import lombok.Getter;

/**
 * 激活码状态
 * @author 技术部
 */

@Getter
public enum AuthCodeStatus {

    no_activate(0, "未激活"),
    yet_activate(1, "已激活");


    private int value;
    private String name;

    private AuthCodeStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
