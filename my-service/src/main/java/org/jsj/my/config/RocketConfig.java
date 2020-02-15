package org.jsj.my.config;

import org.jsj.my.consumer.TxConsumer;
import org.jsj.my.listener.RocketTxListener;
import org.jsj.my.rocket.RocketMessageProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RockeMQ Producer and Consumer
 *
 * @author JSJ
 */
@Configuration
public class RocketConfig {
    @Value("${spring.profiles.active:dev}")
    private String profile;

    @Value("${rocket.name_server.address:localhost:9876}")
    private String nameServerAddr;

    @Value("${rocket.producer.group:Group}")
    private String producerGroup;

    @Value("${rocket.producer.timeout:10000}")
    private int producerTimeout;

    @Value("${rocket.consumer.group:Group}")
    private String consumerGroup;

    @Bean
    public RocketMessageProducer rocketMessageProducer() {
        return new RocketMessageProducer(profile, nameServerAddr, producerGroup, producerTimeout);
    }

    @Bean
    public TxConsumer txConsumer() {
        TxConsumer consumer = new TxConsumer(profile, nameServerAddr, consumerGroup);
        consumer.registerMessageListener(txListener());
        return consumer;
    }

    @Bean
    public RocketTxListener txListener() {
        return new RocketTxListener();
    }

}
