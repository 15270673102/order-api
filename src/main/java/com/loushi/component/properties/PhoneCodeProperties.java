package com.loushi.component.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 验证码
 * @author 技术部
 */

@ConfigurationProperties(prefix = PhoneCodeProperties.PAGEHELPER_PREFIX)
@Data
@Component
public class PhoneCodeProperties {

    public static final String PAGEHELPER_PREFIX = "basis.phone.code";

    /**
     *验证码过期时间 （秒）
     */
    private Integer expiresTime = 300;

}
