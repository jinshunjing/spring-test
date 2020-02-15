package org.jsj.my.config;

import org.jsj.my.listener.RedisTxListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Redis 订阅者
 *
 * @author JSJ
 */
@Configuration
public class RedisConsumerConfig {
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisMessageListenerContainer messageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        container.addMessageListener(
                new MessageListenerAdapter(this.redisTxListener()),
                new ChannelTopic("ch_tx"));

        return container;
    }

    @Bean
    public RedisTxListener redisTxListener() {
        return new RedisTxListener();
    }

}
