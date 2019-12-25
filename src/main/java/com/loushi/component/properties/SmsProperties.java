package com.loushi.component.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sms发短信
 * @author 技术部
 */

@ConfigurationProperties(prefix = SmsProperties.PAGEHELPER_PREFIX)
@Component
@Data
public class SmsProperties {

    public static final String PAGEHELPER_PREFIX = "basis.aliyun.sms";

    private String product;
    private String domain;
    private String accessKeyId;
    private String accessKeySecret;

}
