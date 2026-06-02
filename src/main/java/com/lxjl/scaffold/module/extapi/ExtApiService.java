package com.lxjl.scaffold.module.extapi;

import com.lxjl.scaffold.domain.system.SystemInfoRepo;
import com.lxjl.scaffold.infrastructure.health.DatabaseHealthIndicator;
import com.lxjl.scaffold.infrastructure.time.AppTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Service;

/**
 * 外部 API 服务 — 聚合系统运行指标。
 */
@Service
@RequiredArgsConstructor
public class ExtApiService {

    private final SystemInfoRepo systemInfoRepo;
    private final DatabaseHealthIndicator databaseHealthIndicator;

    public ExtApiDto.MetricsResponse getMetrics() {
        Health dbHealth = databaseHealthIndicator.health();
        Runtime runtime = Runtime.getRuntime();
        return new ExtApiDto.MetricsResponse(
                AppTime.now(),
                systemInfoRepo.current().applicationName(),
                dbHealth.getStatus().getCode(),
                runtime.availableProcessors(),
                runtime.maxMemory() / (1024 * 1024),
                (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        );
    }
}
