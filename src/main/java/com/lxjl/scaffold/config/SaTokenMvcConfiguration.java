package com.lxjl.scaffold.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 路由拦截 — 除白名单外所有 {@code /api/**} 需登录。
 */
@Configuration
public class SaTokenMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle ->
                        SaRouter.match("/api/**")
                                .notMatch("/api/system/ping")
                                .notMatch("/api/system/health")
                                .notMatch("/api/ext/**")
                                .check(r -> StpUtil.checkLogin())))
                .addPathPatterns("/**")
                .excludePathPatterns("/error");
    }
}
