package com.loushi.model;

import lombok.Data;

import java.util.Date;


@Data
public class UserOrderCheck {

    private Integer id;

    private Integer orderId;

    private Integer checkStatus;
    private String checkStatusStr;

    private String checkReason;

    private Date createTime;

}