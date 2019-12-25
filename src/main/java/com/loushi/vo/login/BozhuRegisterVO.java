package com.loushi.vo.login;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 注册VO
 */

@Data
public class BozhuRegisterVO {

    /*
     *用户名
     */
    @NotNull(message = "username不能为空")
    private String username;

    /*
     *密码
     */
    @NotNull(message = "password不能为空")
    private String password;

    /*
     *授权码
     */
    @NotNull(message = "authCode不能为空")
    private String authCode;


    @NotNull(message = "deptId不能为空")
    private Integer deptId;


}
