package io.github.guqiyao.jedis;

import io.github.guqiyao.RedisLockOperation;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;

/**
 * @Author: qiyao.gu
 * @Eamil: qiyao.gu@nalaa.com
 * @Date: 2019/4/19 13:41
 */
public class JedisOperation implements RedisLockOperation {

    private JedisPool jedisPool;

    public JedisOperation(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public boolean lock(String key) {
        return lock(key, RedisLockOperation.DEFAULT_EXPIRE_SECOND);
    }

    @Override
    public boolean lock(String key, int expire) {
        String result = executeCallback(jedis -> jedis.set(key, "write operation", "NX", "EX", expire));

        return StringUtils.isNotBlank(result);
    }

    @Override
    public void unlock(String key) {
        executeCallback(jedis -> jedis.del(key));
    }

    private <T> T executeCallback(Callback<T> callback) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            return callback.execute(jedis);

        } finally {
            if (Objects.nonNull(jedis)) {
                jedis.close();
            }
        }
    }
}