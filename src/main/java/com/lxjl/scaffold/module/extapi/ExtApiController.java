package com.lxjl.scaffold.module.extapi;

import com.lxjl.scaffold.common.web.WebResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外部 API — 供内部管理系统调用（需 HMAC-SHA256 签名验证）。
 *
 * <p>调用示例见项目 README。
 */
@Tag(name = "外部API", description = "内部管理系统接口（HMAC 签名验证）")
@RestController
public class ExtApiController {

    private final ExtApiService extApiService;

    public ExtApiController(ExtApiService extApiService) {
        this.extApiService = extApiService;
    }

    @Operation(summary = "查询系统运行指标", description = "返回应用名、状态、JVM 内存等信息")
    @GetMapping("/api/ext/system/metrics")
    public ResponseEntity<?> metrics() {
        return WebResponses.ok(extApiService.getMetrics());
    }
}
