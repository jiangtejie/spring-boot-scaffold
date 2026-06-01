package com.example.scaffold.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 跨域 + 上传文件静态资源映射。
 */
@Configuration
@RequiredArgsConstructor
public class MvcUploadResourceConfiguration implements WebMvcConfigurer {

    private final AppProperties appProperties;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String dir = appProperties.getUpload().getDir().replace("\\", "/");
        if (!dir.endsWith("/")) dir = dir + "/";
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + dir);
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        if (appProperties.getCors().getAllowedOriginPatterns().isEmpty()) return;
        registry.addMapping("/**")
                .allowedOriginPatterns(appProperties.getCors().getAllowedOriginPatterns().toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
