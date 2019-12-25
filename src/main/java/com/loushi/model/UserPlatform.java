package com.loushi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户平台表
 */

@Data
public class UserPlatform implements Serializable {

    private Integer id;

    private Integer userId;

    private Integer platformType;
    private String platformTypeStr;

    private String accountNickName;

    private String accountUid;

    private Date createTime;

}