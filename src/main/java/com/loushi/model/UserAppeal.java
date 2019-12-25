package com.loushi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户申述表
 * @author 技术部
 */

@Data
public class UserAppeal implements Serializable {

    private Integer id;

    private Integer orderId;

    private Integer role;

    private String content;

    private String pics;

    private Date createTime;
}