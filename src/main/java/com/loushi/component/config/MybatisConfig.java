package com.loushi.component.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis配置
 */

@Configuration
@MapperScan("com.loushi.mapper")
public class MybatisConfig {

}
