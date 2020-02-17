package org.jsj.my.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.constant.WsConstant;
import org.jsj.my.service.MessageAcceptorCache;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

/**
 * 监听客户端连接状态
 *
 * @author JSJ
 */
@Slf4j
@Component
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor sha = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // ignore non-STOMP messages like heartbeat messages
        if (sha.getCommand() == null) {
            return null;
        }

        //判断客户端的连接状态
        switch (sha.getCommand()) {
            case CONNECT:
                connect(sha);
                break;
            case CONNECTED:
                connected(sha);
                break;
            case DISCONNECT:
                disconnect(sha);
                break;
            default:
                break;
        }

        return message;
    }

    /**
     * 连接成功
     */
    private void connect(StompHeaderAccessor sha) {
        String sessionId = sha.getSessionAttributes().get(WsConstant.SESSION_ID).toString();

        List<String> uidList = sha.getNativeHeader(WsConstant.UID);
        String uid = null;
        if (isNotEmpty(uidList)) {
            uid = uidList.get(0);
        }

        log.info("STOMP Connect [{}, {}]", uid, sessionId);

        MessageAcceptorCache.putSessionId(sessionId, uid);
    }

    /**
     * 连接中
     */
    private void connected(StompHeaderAccessor sha) {
        String sessionId = sha.getSessionAttributes().get(WsConstant.SESSION_ID).toString();

        log.info("STOMP Connected [{}]", sessionId);
    }

    /**
     * 断开连接
     */
    private void disconnect(StompHeaderAccessor sha) {
        String sessionId = sha.getSessionAttributes().get(WsConstant.SESSION_ID).toString();
        String uid = MessageAcceptorCache.getUidBySessionId(sessionId);

        log.info("STOMP Disconnect [{}, {}]", uid, sessionId );

        sha.getSessionAttributes().remove(sessionId);
        MessageAcceptorCache.removeUid(sessionId);
        MessageAcceptorCache.removeAcceptor(uid);
    }

}