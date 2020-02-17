package org.jsj.my.config;


import org.jsj.my.interceptor.HttpSessionIdHandshakeInterceptor;
import org.jsj.my.interceptor.PresenceChannelInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * WebSocketConfig
 *
 * @author JSJ
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册stomp的节点,并指定使用SockJS协议
        registry.addEndpoint("/endpointTicker")
                .setAllowedOrigins("*")
                .withSockJS()
                .setInterceptors(httpSessionIdHandshakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 消息代理的三种方式
        registry.enableSimpleBroker("/topic", "/queue", "/user");
        // 点对点消息发送默认配置/user前缀,这里显式给出,默认是有的
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * 消息传输参数配置
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        //设置消息字节数大小
        registry.setMessageSizeLimit(8192)
                //设置消息缓存大小
                .setSendBufferSizeLimit(8192)
                //设置消息发送时间限制毫秒
                .setSendTimeLimit(11000);
    }

    @Bean
    public HttpSessionIdHandshakeInterceptor httpSessionIdHandshakeInterceptor() {
        return new HttpSessionIdHandshakeInterceptor();
    }

    @Bean
    public PresenceChannelInterceptor presenceChannelInterceptor() {
        return new PresenceChannelInterceptor();
    }

    /**
     * 配置客户端入站通道拦截器
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(presenceChannelInterceptor());
    }

}
