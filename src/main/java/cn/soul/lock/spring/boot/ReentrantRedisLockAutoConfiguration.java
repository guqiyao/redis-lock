package cn.soul.lock.spring.boot;

import cn.soul.lock.RedisLockOperation;
import cn.soul.lock.ReentrantRedisLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: qiyao.gu
 * @Eamil: qiyao.gu@nalaa.com
 * @Date: 4/18/2019 5:41 PM
 */
@Configuration
@ConditionalOnBean(value = RedisLockOperation.class)
public class ReentrantRedisLockAutoConfiguration {

    @Bean
    public ReentrantRedisLock reentrantRedisLock(RedisLockOperation redisLockOperation) {
        return new ReentrantRedisLock(redisLockOperation);
    }
}