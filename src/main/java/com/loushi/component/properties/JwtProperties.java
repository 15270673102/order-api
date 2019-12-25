package com.loushi.component.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt认证mdoel
 * @author 技术部
 */

@ConfigurationProperties(prefix = JwtProperties.PAGEHELPER_PREFIX)
@Data
@Component
public class JwtProperties {

    public static final String PAGEHELPER_PREFIX = "basis.jwt";

    /**
     * 秘钥
     */
    private String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    /**
     * 过期时间（秒） 1天
     */
    private Integer expiresSecond = 86400;

}
