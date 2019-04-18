package cn.soul.lock;

import lombok.Getter;

import java.util.concurrent.locks.Lock;

/**
 * @Author: qiyao.gu
 * @Eamil: qiyao.gu@nalaa.com
 * @Date: 2019/4/1 16:00
 */
public class LockWrapper {

    @Getter
    private Lock lock;
    private long lastOperationTime;

    public LockWrapper(Lock lock) {
        this.lock = lock;
        this.lastOperationTime = System.currentTimeMillis();
    }

    long getLastOperationTime() {
        return lastOperationTime;
    }

    void updateLastOperationTime() {
        this.lastOperationTime = System.currentTimeMillis();
    }
}