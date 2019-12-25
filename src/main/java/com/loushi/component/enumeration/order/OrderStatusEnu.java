package com.loushi.component.enumeration.order;

import lombok.Getter;

/**
 * 订单状态
 * @author 技术部
 */

@Getter
public enum OrderStatusEnu {

    all(0, "全部"),
    process(1, "已接单"),
    finish(2, "已完成"),

    sys_check_faild(3, "1审失败"),
    sys_2_check_faild(12, "2审失败"),
    sys_2_check_skip(13, "2审跳过"),
    sys_2_check_success(14, "2审成功"),

    artificial_audit_success(21, "人工核对成功"),
    artificial_audit_faild(22, "人工核对失败"),

    person_check_faild(4, "人工审核失败"),
    person_appealing(5, "用户申诉中");


    private int value;
    private String name;

    private OrderStatusEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getByValue(int value) {
        for (OrderStatusEnu status : values()) {
            if (status.getValue() == value) {
                return status.getName();
            }
        }
        return null;
    }

}
