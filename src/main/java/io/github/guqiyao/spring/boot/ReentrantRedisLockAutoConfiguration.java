package io.github.guqiyao.spring.boot;

import io.github.guqiyao.RedisLockOperation;
import io.github.guqiyao.ReentrantRedisLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: qiyao.gu
 * @Eamil: 125307126@qq.com
 * @Date: 4/18/2019 5:41 PM
 */
@Configuration
public class ReentrantRedisLockAutoConfiguration {

    @Bean
    @ConditionalOnBean(value = RedisLockOperation.class)
    public ReentrantRedisLock reentrantRedisLock(RedisLockOperation redisLockOperation) {
        return new ReentrantRedisLock(redisLockOperation);
    }
}