package com.loushi.component.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sts文件上传
 * @author 技术部
 */

@ConfigurationProperties(prefix = StsProperties.PAGEHELPER_PREFIX)
@Component
@Data
public class StsProperties {

    public static final String PAGEHELPER_PREFIX = "basis.sts";

    private String stsEndpoint;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String roleArn;

}
