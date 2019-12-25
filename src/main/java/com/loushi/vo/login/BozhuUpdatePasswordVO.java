package com.loushi.vo.login;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BozhuUpdatePasswordVO extends SendCodeVO {

    /*
     *验证码
     */
    @NotNull(message = "code不能为空")
    private String code;

    /*
     *账号
     */
    @NotNull(message = "username不能为空")
    private String username;

    /*
     *密码
     */
    @NotNull(message = "password不能为空")
    private String password;

}
