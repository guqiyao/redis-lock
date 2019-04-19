package io.github.guqiyao.jedis;

import redis.clients.jedis.Jedis;

/**
 * @Author: qiyao.gu
 * @Eamil: qiyao.gu@nalaa.com
 * @Date: 2019/4/19 13:48
 */
public interface Callback<T> {

    T execute(Jedis jedis);
}