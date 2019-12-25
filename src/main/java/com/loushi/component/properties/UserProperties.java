package com.loushi.component.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用户基本数据
 * @author 技术部
 */

@ConfigurationProperties(prefix = UserProperties.PAGEHELPER_PREFIX)
@Component
@Data
public class UserProperties {

    public static final String PAGEHELPER_PREFIX = "basis.user";

    /**
     * 起始次数
     */
    private Integer creditNum = 3;

}
