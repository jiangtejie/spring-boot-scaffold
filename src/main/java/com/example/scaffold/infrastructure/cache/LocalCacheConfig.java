package com.example.scaffold.infrastructure.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Guava 本地缓存配置 — 用于热点数据本地缓存，减少 Redis 调用。
 *
 * <p>使用示例：
 * <pre>{@code
 * @Autowired
 * private Cache<String, UserDto> localCache;
 *
 * UserDto user = localCache.get("user:123", () -> userService.loadFromDb(123L));
 * }</pre>
 */
@Configuration
public class LocalCacheConfig {

    /**
     * 通用本地缓存：最大 1000 条，写入后 5 分钟过期。
     */
    @Bean
    public Cache<String, Object> localCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }
}
