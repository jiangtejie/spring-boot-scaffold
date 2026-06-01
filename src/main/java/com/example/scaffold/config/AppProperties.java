package com.example.scaffold.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用级配置属性，绑定 {@code app.*} 前缀。
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Page page = new Page();
    private final Cors cors = new Cors();
    private final Upload upload = new Upload();
    private final ExtApi extApi = new ExtApi();

    @Data
    public static class Page {
        private int defaultSize = 20;
        private int maxSize = 100;
    }

    @Data
    public static class Cors {
        private List<String> allowedOriginPatterns = new ArrayList<>(List.of("*"));
    }

    @Data
    public static class Upload {
        private String dir = "./uploads";
        private String baseUrl = "http://localhost:8080/uploads";
    }

    @Data
    public static class ExtApi {
        /** 外部 API HMAC 签名密钥（部署时通过 EXTERNAL_API_SECRET 注入） */
        private String secret = "";
    }
}
