package org.jsj.my.rocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * RocketMQ Consumer
 *
 * @author JSJ
 */
@Slf4j
public abstract class RocketMessageConsumer {
    protected DefaultMQPushConsumer consumer;

    protected String topic;

    protected boolean started;

    protected RocketMessageConsumer(String profile, String nameServerAddr, String group, String topic) {
        consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(nameServerAddr);
        this.topic = profile + topic;

        // 不使用VIP
        consumer.setVipChannelEnabled(false);

        started = false;
    }

    public void registerMessageListener(MessageListenerConcurrently listener) {
        consumer.registerMessageListener(listener);
    }

    public void registerMessageListener(MessageListenerOrderly listener) {
        consumer.registerMessageListener(listener);
    }

    public void start() {
        if (started) {
            return;
        }
        try {
            consumer.subscribe(topic, "*");
            consumer.start();
            started = true;
            log.info("Rocket MQ Consumer started: {}, {}, {}",
                    consumer.getConsumerGroup(), topic, consumer.getNamesrvAddr());
        } catch (MQClientException e) {
            log.error("MQ error", e);
        }
    }

    public void shutdown() {
        consumer.shutdown();
        started = false;
        log.info("Rocket MQ Consumer shut down");
    }
}
