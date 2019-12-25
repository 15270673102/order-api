package com.loushi.component.config;

import com.loushi.component.filter.JwtFilter;
import com.loushi.component.properties.JwtProperties;
import com.loushi.mapper.UsersMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * jwt
 */

@Configuration
public class JwtConfig {

    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private UsersMapper userMapper;

    /**
     * 要保护的url
     */
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> rbean = new FilterRegistrationBean<>();
        rbean.setFilter(new JwtFilter(jwtProperties, userMapper));
        rbean.addUrlPatterns("/taskCenter/*");
        rbean.addUrlPatterns("/userCenter/*");
        rbean.addUrlPatterns("/orderCenter/*");
        rbean.addUrlPatterns("/utils/*");
        return rbean;
    }

}
