package org.jsj.my.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsj.my.constant.WsConstant;
import org.jsj.my.model.MessageAcceptor;
import org.jsj.my.service.MessageAcceptorCache;
import org.jsj.my.service.TickerReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * webSocket接口实现
 *
 * @author JSJ
 */
@Slf4j
@Controller
@Api(description = "WebSocket API")
public class WsController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private TickerReadService tickerService;

    /**
     * 注册发送消息
     * 用户获取新数据,每次都要重新请求这个接口
     *
     * @param acceptor 参数
     */
    @MessageMapping("message")
    @ApiOperation(value = "subscribe the topic")
    public void getTickers(MessageAcceptor acceptor) {
        log.info("message: {}", acceptor);
        if (acceptor == null) {
            return;
        }

        // FIXME: 防止DOS攻击
        String uid = acceptor.getUid();
        if (StringUtils.isEmpty(uid)) {

            // FIXME: 匿名用户
            uid = WsConstant.DEFAULT_UID;
            boolean flag = MessageAcceptorCache.updateAnonymous(uid, acceptor);
            if (!flag) {
                return;
            }
        } else {
            // 将用户的消息放到缓存里
            boolean flag = MessageAcceptorCache.update(uid, acceptor);
            if (!flag) {
                return;
            }
        }

        // 不走消息代理,点对点发送.
        // 客户端destinationUrl = /user/{uid}/topic/ticker
        template.convertAndSendToUser(
                uid, "/topic/ticker",
                tickerService.getTickers(acceptor)
        );
    }

}











