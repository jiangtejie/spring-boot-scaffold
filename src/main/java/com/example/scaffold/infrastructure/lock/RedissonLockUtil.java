package com.example.scaffold.infrastructure.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * Redisson 分布式锁工具。
 *
 * <pre>{@code
 * // 自动加锁/解锁
 * String result = lockUtil.executeWithLock("order:123", () -> processOrder(123L));
 *
 * // 手动控制
 * if (lockUtil.tryLock("order:123", 10, 30)) {
 *     try { processOrder(123L); }
 *     finally { lockUtil.unlock("order:123"); }
 * }
 * }</pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockUtil {

    private final RedissonClient redissonClient;

    /** 默认等待时间 10 秒，锁持有时间 30 秒 */
    public boolean tryLock(String key) {
        return tryLock(key, 10, 30);
    }

    public boolean tryLock(String key, long waitSec, long leaseSec) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitSec, leaseSec, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /** 带锁执行业务逻辑，自动释放 */
    public <T> T executeWithLock(String key, Supplier<T> action) {
        RLock lock = redissonClient.getLock(key);
        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                return action.get();
            }
            throw new RuntimeException("获取分布式锁失败: " + key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取分布式锁被中断: " + key);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
