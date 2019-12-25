package com.loushi.vo.common;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 微信唯一ID
 * @author 技术部
 */

@Data
public class OpenIdVO {

    /*
     * 微信唯一ID
     */
    @NotNull(message = "openId不能为空")
    private String openId;

}
