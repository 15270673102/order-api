package com.loushi.model;

import lombok.Data;

import java.util.Date;

/**
 * 用户表
 */

@Data
public class Users {

    private Integer id;

    private String phone;

    private String password;

    private String nickName;

    private Integer userRole;

    private Integer userStatus;

    private Integer creditNum;

    private String realName;

    private String wechat;

    private String alipay;

    private Integer deptId;

    private String activeCode;

    private Date createTime;


    //部门
    private String deptName;

}