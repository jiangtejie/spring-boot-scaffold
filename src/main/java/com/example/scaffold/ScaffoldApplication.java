package com.example.scaffold;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 后端脚手架启动入口。
 *
 * <p>内置：
 * <ul>
 *   <li>统一 API 响应 {@code ApiResult<T>}</li>
 *   <li>全局异常处理 + Sa-Token 认证异常拦截</li>
 *   <li>通用分页封装（PageQuery / PageResult）</li>
 *   <li>HMAC-SHA256 外部 API 签名验证</li>
 *   <li>文件上传（本地存储）</li>
 *   <li>数据脱敏 / 接口防刷 / 字符串工具 / 参数校验</li>
 *   <li>OpenAPI 文档（springdoc）</li>
 *   <li>Flyway 数据库迁移</li>
 * </ul>
 *
 * <p>业务开发只需在 {@code domain/} 添加实体、{@code persistence/mapper/} 添加 Mapper、
 * {@code module/} 下新建业务模块即可。
 */
@SpringBootApplication
@MapperScan("com.example.scaffold.persistence.mapper")
public class ScaffoldApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScaffoldApplication.class, args);
    }
}
