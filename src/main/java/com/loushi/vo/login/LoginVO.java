package com.loushi.vo.login;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

    /**
     * 账号
     */
    @NotNull(message = "username不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "password不能为空")
    private String password;

    /**
     * 身份
     */
    @NotNull(message = "userRole不能为空")
    private Integer userRole;
}
