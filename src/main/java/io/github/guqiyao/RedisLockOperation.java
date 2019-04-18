package io.github.guqiyao;

/**
 * @Author: qiyao.gu
 * @Eamil: 125307126@qq.com
 * @Date: 2019/4/2 17:05
 */
public interface RedisLockOperation {

    /**
     * 加锁(默认过期时间30秒)
     * @param key   key
     * @return      成功或失败
     */
    boolean lock(String key);

    /**
     * 枷锁
     * @param key       key
     * @param expire    过期时间
     * @return          成功或失败
     */
    boolean lock(String key, int expire);

    /**
     * 解锁
     * @param key       key
     */
    void unlock(String key);
}