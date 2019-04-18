package cn.soul.lock.spring;

import cn.soul.lock.RedisLockOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

/**
 * @Author: qiyao.gu
 * @Eamil: 125307126@qq.com
 * @Date: 2019/4/2 17:08
 */
public class RedisTemplateOperation<K, V> implements RedisLockOperation {

    private static final int DEFAULT_EXPIRE_SECOND = 30;

    private RedisTemplate<K, V> redisTemplate;

    public RedisTemplateOperation(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean lock(String key) {
        return lock(key, DEFAULT_EXPIRE_SECOND);
    }

    @Override
    public boolean lock(String key, int expire) {
        String result = redisTemplate.execute((RedisCallback<String>) connection -> {
            Jedis jedis = (Jedis) connection.getNativeConnection();
            return jedis.set(key, "write operation", "NX", "EX", expire);
        });

        return StringUtils.isNotBlank(result);
    }

    @Override
    public void unlock(String key) {
        redisTemplate.execute((RedisCallback<String>) connection -> {
            Jedis jedis = (Jedis) connection.getNativeConnection();

            jedis.del(key);

            return null;
        });
    }

}