package io.github.guqiyao;

import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * @Author: qiyao.gu
 * @Eamil: 125307126@qq.com
 * @Date: 2019/4/1 15:57
 */
public class ReentrantRedisLock {

    private static final int DEFAULT_LOCKED_TIME = 30;

    private final Map<String, LockWrapper> lockCache = new ConcurrentHashMap<>();

    private RedisLockOperation redisLockOperation;

    public ReentrantRedisLock(RedisLockOperation redisLockOperation) {
        this.redisLockOperation = redisLockOperation;
        new CleanUpTimer().runTask();
    }

    public Lock getLock(String key) {
        return getLock(key, DEFAULT_LOCKED_TIME);
    }

    public Lock getLock(String key, int lockedTime) {
        LockWrapper lockWrapper = lockCache.get(key);
        if (Objects.nonNull(lockWrapper)) {
            lockWrapper.updateLastOperationTime();
            return lockWrapper.getLock();
        }

        Lock redisLock = new RedisLock(redisLockOperation, key, lockedTime);
        lockWrapper = lockCache.putIfAbsent(key, new LockWrapper(redisLock));
        if (Objects.isNull(lockWrapper)) {
            return redisLock;
        }

        lockWrapper.updateLastOperationTime();

        return lockWrapper.getLock();
    }

    private class CleanUpTimer {
        private static final long DELAY = 60000L;
        private static final long PERIOD = 60000L;
        private static final long EXPIRE_TIME = 180000L;

        private Timer timer = new Timer();

        private void runTask() {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (lockCache.isEmpty()) {
                        return;
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    for (Map.Entry<String, LockWrapper> entry : lockCache.entrySet()) {
                        LockWrapper lockWrapper = entry.getValue();
                        long lastOperationTime = lockWrapper.getLastOperationTime();
                        if (currentTimeMillis - lastOperationTime > EXPIRE_TIME) {
                            lockCache.remove(entry.getKey());
                        }
                    }
                }

            }, DELAY, PERIOD);
        }
    }

}