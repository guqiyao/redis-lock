package cn.soul.lock;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author: qiyao.gu
 * @Eamil: qiyao.gu@nalaa.com
 * @Date: 2019/4/1 16:04
 */
public class RedisLock implements Lock {

    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();
    private static final Object IDENTIFIER = new Object();
    private static final long INTERVAL_TIME_MILLIS = 500L;

    private String key;
    private int lockedTime;
    private RedisLockOperation redisLockOperation;

    RedisLock(RedisLockOperation redisLockOperation, String key, int lockedTime) {
        this.redisLockOperation = redisLockOperation;
        this.lockedTime = lockedTime;
        this.key = key;
    }

    @Override
    public void lock() {
        if (isReentrant()) {
            return;
        }

        while (!toLock()) {
            try {
                Thread.sleep(INTERVAL_TIME_MILLIS);
            } catch (InterruptedException e) {
                //Nothing to do
            }
        }

        THREAD_LOCAL.set(IDENTIFIER);
    }

    @Override
    public boolean tryLock() {
        if (isReentrant()) {
            return true;
        }

        boolean result = toLock();
        if (result) {
            THREAD_LOCAL.set(IDENTIFIER);
        }

        return result;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        if (isReentrant()) {
            return true;
        }

        long nanosTimeout = unit.toNanos(time);
        final long deadline = System.nanoTime() + nanosTimeout;

        while (true) {
            boolean result = toLock();
            if (result) {
                THREAD_LOCAL.set(IDENTIFIER);
                return true;
            }

            nanosTimeout = deadline - System.nanoTime();
            if (nanosTimeout <= 0) {
                return false;
            }
        }

    }

    @Override
    public void unlock() {
        if (Objects.isNull(THREAD_LOCAL.get())) {
            return;
        }

        try {
            redisLockOperation.unlock(key);
        } finally {
            THREAD_LOCAL.remove();
        }
    }

    @Override
    public void lockInterruptibly() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    private boolean toLock() {
        return redisLockOperation.lock(key, lockedTime);
    }

    private boolean isReentrant() {
        return Objects.nonNull(THREAD_LOCAL.get());
    }
}