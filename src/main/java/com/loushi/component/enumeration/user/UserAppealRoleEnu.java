package com.loushi.component.enumeration.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户申述角色
 * @author 技术部
 */

public enum UserAppealRoleEnu {

    user(1, "用户"),
    admin(2, "管理员");


    @Setter
    @Getter
    private int value;
    @Setter
    @Getter
    private String name;

    private UserAppealRoleEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
