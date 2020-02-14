package org.jsj.my.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁
 *
 * @author The Flow Developers
 */
@Slf4j
public class RedisLock {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 加锁
     *
     * @param key
     * @param value
     * @param expireSec
     * @return
     */
    public Boolean lock(String key, String value, Long expireSec) {
        SessionCallback<Boolean> sessionCallback = new SessionCallback<Boolean>() {
            List<Object> exec = null;
            @Override
            @SuppressWarnings("unchecked")
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                redisTemplate.opsForValue().setIfAbsent(key, value);
                redisTemplate.expire(key, expireSec, TimeUnit.SECONDS);
                exec = operations.exec();
                if(exec.size() > 0) {
                    return (Boolean) exec.get(0);
                }
                return false;
            }
        };
        return redisTemplate.execute(sessionCallback);
    }

    public Boolean lock(String key, String value) {
        return lock(key, value, 30L);
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean unlock(String key, String value) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(UNLOCK_LUA);
        redisScript.setResultType(Boolean.class);
        return redisTemplate.execute(redisScript, Arrays.asList(key), value);
    }

    private static final String UNLOCK_LUA =
            "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 检查是否已经上锁
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean checkLock(String key, String value) {
        String val = redisTemplate.opsForValue().get(key);
        return value.equals(val);
    }

    /**
     * 强制加锁，有效期30s
     * @param key
     * @param value
     */
    public void forceLock(String key, String value) {
        log.info("Force lock: {}, {}", key, value);
        redisTemplate.opsForValue().set(key, value, 30, TimeUnit.SECONDS);
    }
    public void forceLock(String key, String value, int expire) {
        log.info("Force lock: {}, {}", key, value);
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }
}
