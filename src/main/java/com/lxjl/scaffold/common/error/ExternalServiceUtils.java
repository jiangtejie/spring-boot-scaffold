package com.lxjl.scaffold.common.error;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

/**
 * 外部服务调用工具类 — 带重试机制和统一异常处理。
 *
 * <p>当前为简易版阻塞重试，生产环境建议迁移至 Resilience4j {@code @Retry}。
 */
@Slf4j
public final class ExternalServiceUtils {

    private ExternalServiceUtils() {}

    public static <T> T executeWithRetry(Supplier<T> operation, String serviceName, int maxRetries) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                return operation.get();
            } catch (Exception e) {
                attempt++;
                log.warn("[{}] 第 {}/{} 次调用失败: {}", serviceName, attempt, maxRetries, e.getMessage());
                if (attempt >= maxRetries) {
                    throw new AppBizException(AppErrorCode.THIRD_PARTY_SERVICE_ERROR,
                            String.format("%s服务调用失败（重试%d次）: %s", serviceName, maxRetries, e.getMessage()));
                }
                try {
                    Thread.sleep(1000L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new AppBizException(AppErrorCode.THIRD_PARTY_SERVICE_ERROR,
                            serviceName + " 调用被中断");
                }
            }
        }
        throw new AppBizException(AppErrorCode.THIRD_PARTY_SERVICE_ERROR, serviceName + "服务调用失败");
    }

    public static <T> T executeWithRetry(Supplier<T> operation, String serviceName) {
        return executeWithRetry(operation, serviceName, 3);
    }

    public static <T> T executeSafely(Supplier<T> operation, T defaultValue, String serviceName) {
        try {
            return operation.get();
        } catch (Exception e) {
            log.warn("[{}] 调用失败，使用默认值: {}", serviceName, e.getMessage());
            return defaultValue;
        }
    }
}
