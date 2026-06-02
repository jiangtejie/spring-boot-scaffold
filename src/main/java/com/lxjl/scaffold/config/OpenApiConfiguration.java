package com.lxjl.scaffold.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI 配置 — Swagger UI 访问：{@code /swagger-ui.html}
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI scaffoldOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Scaffold API")
                        .version("1.0.0")
                        .description("Spring Boot 脚手架接口文档。成功响应格式：`{ \"code\": 200, \"msg\": \"ok\", \"data\": ... }`")
                        .contact(new Contact().name("Scaffold")))
                .servers(List.of(new Server().url("/").description("当前服务")));
    }
}
