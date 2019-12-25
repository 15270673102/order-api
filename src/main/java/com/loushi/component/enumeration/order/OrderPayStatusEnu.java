package com.loushi.component.enumeration.order;

import lombok.Getter;

/**
 * 订单状态
 * @author 技术部
 */

@Getter
public enum OrderPayStatusEnu {

    no_balance(0, "未结算"),
    balanceing(1, "结算中"),
    yet_balance(2, "结算完成"),

    freeze(3, "冻结"),
    abnormal(4, "异常");


    private int value;
    private String name;

    private OrderPayStatusEnu(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
