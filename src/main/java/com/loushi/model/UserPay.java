package com.loushi.model;

import lombok.Data;

import java.util.Date;

/**
 * 结算表
 */

@Data
public class UserPay {

    private Integer id;

    private String payNo;

    private Integer orderId;

    private Double money;

    private Date payTime;

    private Integer payStatus;

    private Date createTime;

    private UserTask task;
}