package com.loushi.vo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户授权和登入 状态VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginStateVO {

    /**
     * 状态
     */
    private Integer status;
    /**
     * 状态信息
     */
    private String msg;

    /**
     * openId
     */
    private String openId;

    /**
     * Object
     */
    private Object object;

}
