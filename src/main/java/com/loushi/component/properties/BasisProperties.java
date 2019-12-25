package com.loushi.component.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = BasisProperties.PAGEHELPER_PREFIX)
@Component
@Data
public class BasisProperties {

    public static final String PAGEHELPER_PREFIX = "basis.sys";

    /**
     * 最大绑定多少个平台数量
     */
    private Integer maxPlatFormNum = 6;

    /**
     * 1审延迟默认时间
     */
    private Integer oneSysAuditDelaySecond = 120;

    //开发环境
    private String dev = "dev";
    private String test = "test";
    private String pro = "pro";

}
