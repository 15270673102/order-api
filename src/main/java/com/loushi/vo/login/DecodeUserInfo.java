package com.loushi.vo.login;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * decodeUserInfo
 */

@Data
public class DecodeUserInfo {

    /*
     *明文加密数据
     */
    @NotNull(message = "encryptedData不能为空")
    private String encryptedData;

    /*
     *加密算法的初始向量
     */
    @NotNull(message = "iv不能为空")
    private String iv;

    /*
     *用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key
     */
    @NotNull(message = "code不能为空")
    private String code;

}
