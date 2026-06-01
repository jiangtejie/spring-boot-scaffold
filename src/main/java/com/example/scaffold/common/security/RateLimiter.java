package com.example.scaffold.common.security;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 接口防刷 — 基于 Redis 的请求频率限制。
 */
@Slf4j
@Component
public class RateLimiter {

    private final RedisTemplate<String, String> redisTemplate;

    public RateLimiter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 检查是否允许请求。
     *
     * @param key         限流标识（如 "user:123:api:login"）
     * @param maxRequests 最大请求次数
     * @param timeWindow  时间窗口（秒）
     * @return true=放行
     */
    public boolean allowRequest(String key, int maxRequests, int timeWindow) {
        try {
            String redisKey = "rate_limit:" + key;
            Long count = redisTemplate.opsForValue().increment(redisKey);
            if (count == null) return false;
            if (count == 1) redisTemplate.expire(redisKey, timeWindow, TimeUnit.SECONDS);
            boolean allowed = count <= maxRequests;
            if (!allowed) log.warn("[防刷] key={}, count={}/{}", key, count, maxRequests);
            return allowed;
        } catch (Exception e) {
            log.error("[防刷] Redis 操作失败: {}", e.getMessage());
            return true; // 降级放行
        }
    }

    public void clear(String key) {
        try {
            redisTemplate.delete("rate_limit:" + key);
        } catch (Exception e) {
            log.error("[防刷] 清除失败: {}", e.getMessage());
        }
    }
}
