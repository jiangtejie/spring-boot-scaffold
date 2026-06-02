package com.lxjl.scaffold.config;

import com.lxjl.scaffold.domain.system.SystemInfo;
import com.lxjl.scaffold.domain.system.SystemInfoRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemInfoConfiguration {

    @Bean
    SystemInfoRepo systemInfoRepo(@Value("${spring.application.name}") String applicationName) {
        return () -> new SystemInfo(applicationName);
    }
}
