package org.jsj.my.listener;

import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Rocket MQ 消息监听器
 *
 * @author JSJ
 */
@Slf4j
public class RocketTxListener implements MessageListenerConcurrently {
    @Autowired
    private EventBus eventBus;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                    ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        log.info("Consume message: {}", msgs.size());
        for (MessageExt msg : msgs) {
            String text = new String(msg.getBody());
            process(text, msg.getTags());
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    private void process(String text, String tags) {
        // TODO 构造异步处理的事件
        //eventBus.post();
    }
}
