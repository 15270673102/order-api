package com.loushi.vo.login;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发验证码VO
 */

@Data
public class SendCodeVO {

    /**
     *手机号码
     */
    @NotNull(message = "phoneNum不能为空")
    private String phoneNum;

}
