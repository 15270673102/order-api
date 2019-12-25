package com.loushi.component.properties;

import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * python地址
 * @author 技术部
 */

@ConfigurationProperties(prefix = CommonPythonProperties.PAGEHELPER_PREFIX)
@Data
@Component
public class CommonPythonProperties {

    public static final String PAGEHELPER_PREFIX = "basis.common.python";

    private String host = "localhost";

    private String port = "5000";

    private String prefix = "/api";

    //http://www.baidu.com:80
    private String url = "";

    private String baseUrl;

    public String getBaseUrl() {
        String baseUrl = url;
        if (Strings.isNullOrEmpty(url)) {
            baseUrl = String.format("http://%s:%s%s", host, port, prefix);
        }
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl;
    }

}
