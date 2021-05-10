package io.github.guqiyao.spring;

import io.github.guqiyao.RedisLockOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import redis.clients.jedis.Jedis;

/**
 * @Author: qiyao.gu
 * @Eamil: 125307126@qq.com
 * @Date: 2019/4/2 17:08
 */
public class RedisTemplateOperation<K, V> implements RedisLockOperation {

    private RedisTemplate<K, V> redisTemplate;

    public RedisTemplateOperation(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean lock(String key, int expire) {

        Boolean result = redisTemplate.execute(
                (RedisCallback<Boolean>) connection ->
                        connection.set(
                                key.getBytes(),
                                "write operation".getBytes(),
                                Expiration.seconds(expire),
                                RedisStringCommands.SetOption.ifAbsent()
                        )
        );

        return result;
    }

    @Override
    public void unlock(String key) {
        redisTemplate.execute((RedisCallback<Long>) connection -> connection.del(key.getBytes()));
    }
}