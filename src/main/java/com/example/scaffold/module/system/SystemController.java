package com.example.scaffold.module.system;

import com.example.scaffold.common.web.WebResponses;
import com.example.scaffold.domain.system.SystemInfoRepo;
import com.example.scaffold.infrastructure.health.DatabaseHealthIndicator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统端点 — Ping / Health（匿名访问）。
 */
@Tag(name = "系统", description = "存活探测与健康检查（匿名）")
@RestController
@RequiredArgsConstructor
public class SystemController {

    private final SystemInfoRepo systemInfoRepo;
    private final DatabaseHealthIndicator databaseHealthIndicator;

    @Value("${spring.application.name}")
    private String applicationName;

    @Operation(summary = "Ping", description = "轻量存活检查")
    @GetMapping("/api/system/ping")
    public ResponseEntity<?> ping() {
        var info = systemInfoRepo.current();
        return WebResponses.ok(new SystemDto.PingResponse(info.applicationName(), "UP"));
    }

    @Operation(summary = "健康检查", description = "聚合应用状态与数据库健康明细")
    @GetMapping("/api/system/health")
    public ResponseEntity<?> health() {
        Health health = databaseHealthIndicator.health();
        Map<String, Object> response = new HashMap<>();
        response.put("application", applicationName);
        response.put("status", health.getStatus().getCode());
        response.put("database", health.getDetails());
        return WebResponses.ok(response);
    }
}
