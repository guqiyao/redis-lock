package io.github.guqiyao;

/**
 * @Author: qiyao.gu
 * @Eamil: qiyao.gu@nalaa.com
 * @Date: 2019/4/25 17:06
 */
public class RedisLockException extends RuntimeException {

    public RedisLockException(Throwable throwable) {
        super(throwable);
    }
}