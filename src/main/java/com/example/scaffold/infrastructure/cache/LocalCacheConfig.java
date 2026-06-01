package com.example.scaffold.infrastructure.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Caffeine 本地缓存配置。
 *
 * <h3>声明式（推荐）</h3>
 * <pre>{@code
 * @Cacheable(value = "users", key = "#userId")
 * public UserDto getUser(Long userId) { ... }
 *
 * @CacheEvict(value = "users", key = "#userId")
 * public void updateUser(Long userId) { ... }
 * }</pre>
 *
 * <h3>编程式</h3>
 * <pre>{@code
 * @Autowired
 * private Cache<String, Object> localCache;
 *
 * UserDto user = (UserDto) localCache.get("user:123", k -> loadFromDb(123L));
 * }</pre>
 */
@Configuration
public class LocalCacheConfig {

    /**
     * Spring CacheManager — 支持 {@code @Cacheable} 注解。
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats());
        return manager;
    }

    /**
     * 编程式 Caffeine Cache — 直接注入使用。
     */
    @Bean
    public Cache<String, Object> localCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }
}
