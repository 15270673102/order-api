package com.loushi.vo.login;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 注册VO
 */

@Data
public class UserUpdatePasswordVO extends SendCodeVO {

    /*
     *验证码
     */
    @NotNull(message = "code不能为空")
    private String code;

    /*
     *密码
     */
    @NotNull(message = "password不能为空")
    private String password;

}
