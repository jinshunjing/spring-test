package org.jsj.my.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Redis 消息的处理
 *
 * @author JSJ
 */
@Slf4j
public class RedisTxListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.toString();

        // TODO 业务逻辑
    }

}
