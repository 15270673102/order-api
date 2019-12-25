package com.loushi.component.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 小程序
 * @author 技术部
 */

@ConfigurationProperties(prefix = WxAppletProperties.PAGEHELPER_PREFIX)
@Data
@Component
public class WxAppletProperties {

    public static final String PAGEHELPER_PREFIX = "basis.wx.applet";

    /**
     * appid
     */
    private String appId;
    /*
     *秘钥
     */
    private String appSecret;

}
