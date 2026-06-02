package com.lxjl.scaffold.config;

import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisFlexConfig {

    @Bean
    public MyBatisFlexCustomizer myBatisFlexCustomizer() {
        return config -> {
            // 自定义配置（可选）
        };
    }
}
