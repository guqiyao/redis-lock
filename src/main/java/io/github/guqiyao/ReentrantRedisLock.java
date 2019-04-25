package io.github.guqiyao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @Author: qiyao.gu
 * @Eamil: 125307126@qq.com
 * @Date: 2019/4/1 15:57
 */
public class ReentrantRedisLock {

    private static final int DEFAULT_LOCKED_TIME = 30;

    private final Cache<String, RedisLock> cache = CacheBuilder.newBuilder()
            .maximumSize(100L)
            .expireAfterAccess(180000L, TimeUnit.MILLISECONDS)
            .build();

    private RedisLockOperation redisLockOperation;

    public ReentrantRedisLock(RedisLockOperation redisLockOperation) {
        this.redisLockOperation = redisLockOperation;
    }

    public Lock getLock(String key) {
        return getLock(key, DEFAULT_LOCKED_TIME);
    }

    public Lock getLock(String key, int lockedTime) {
        try {
            return cache.get(key, () -> new RedisLock(redisLockOperation, key, lockedTime));
        } catch (ExecutionException e) {
            throw new RedisLockException(e);
        }
    }

}