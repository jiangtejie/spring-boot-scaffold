package com.lxjl.scaffold.module.extapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 外部 API 数据传输对象。
 */
public final class ExtApiDto {

    private ExtApiDto() {}

    @Schema(description = "系统运行指标")
    public record MetricsResponse(
            @Schema(description = "指标生成时间")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime generatedAt,

            @Schema(description = "应用名称", example = "scaffold")
            String applicationName,

            @Schema(description = "服务状态", example = "UP")
            String status,

            @Schema(description = "JVM 可用处理器数", example = "8")
            int availableProcessors,

            @Schema(description = "JVM 最大内存 (MB)", example = "4096")
            long maxMemoryMb,

            @Schema(description = "JVM 已用内存 (MB)", example = "256")
            long usedMemoryMb
    ) {}
}
