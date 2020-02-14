package org.jsj.my.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis MQ Publisher
 *
 * @author The flow developers
 */
@Slf4j
public class RedisMessagePublisher {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void publish(String topic, String message) {
        redisTemplate.convertAndSend(topic, message);
        log.info("published: {}, {}", topic, message);
    }

}
