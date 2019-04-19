# redis-lock
基于Redis并简单实现了java.util.concurrent.locks.Lock接口的Lock

## 使用方式
### spring boot
#### RedisTemplate(目前只支持RedisTemplate的封装)

* 配置
```
    @Bean
    public RedisLockOperation redisLockOperation(RedisTemplate<String, String> redisTemplate) {
        return new RedisTemplateOperation<>(redisTemplate);
    }
```


* 使用
```
    public class TestServiceImpl {
    
        @Autowired
        private ReentrantRedisLock reentrantRedisLock;
        
        public void use() {
            String key = "test_ley";
            //未指定过期时间默认是30秒过期
            Lock lock = reentrantRedisLock.getLock(key);
            
            try {
                lock.lock();
                //TODO
            } finally {
                lock.unlock();
            }
        }
        
        public  void useWithExpireTime() {
            String key = "test_ley";
            //指定过期时间为10秒
            Lock lock = reentrantRedisLock.getLock(key, 10);
            
            try {
                lock.lock();
                //TODO
            } finally {
                lock.unlock();
            }
        }
    
    }
```

* 注:

1. lockInterruptibly 与 newCondition 方法未实现;