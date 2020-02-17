package org.jsj.my.schedule;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.model.MessageAcceptor;
import org.jsj.my.service.MessageAcceptorCache;
import org.jsj.my.service.TickerReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 行情推送
 *
 * @author JSJ
 */
@Slf4j
@Component
public class TickerPushJob {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private TickerReadService tickerService;

    /**
     * 遍历缓存,根据用户的参数定时发送消息
     */
    @Scheduled(cron = "17 * * * * ?")
    public void pushUser() {
        ConcurrentHashMap<String, MessageAcceptor> params = MessageAcceptorCache.getParams();
        if (params.isEmpty()) {
            return;
        }

        log.info("pushUser entrance: connections = {}", params.size());
        long st = System.currentTimeMillis();

        for (Map.Entry<String, MessageAcceptor> v : params.entrySet()) {
            try {
                MessageAcceptor acceptor = v.getValue();

                template.convertAndSendToUser(
                        v.getKey(), "/topic/ticker",
                        tickerService.getTickers(acceptor)
                );
            } catch (Exception e) {
                log.error("pushUser error", e);
            }
        }

        long ct = System.currentTimeMillis() - st;
        log.info("pushUser exit: cost = {}ms", ct);
    }

    /**
     * 遍历缓存,根据用户的参数定时发送消息
     */
    @Scheduled(cron = "47 * * * * ?")
    public void pushAnonymous() {
        ConcurrentHashMap<String, MessageAcceptor> params = MessageAcceptorCache.getAnonymous();
        if (params.isEmpty()) {
            return;
        }

        log.info("pushAnonymous entrance: connections = {}", params.size());
        long st = System.currentTimeMillis();

        for (Map.Entry<String, MessageAcceptor> v : params.entrySet()) {
            try {
                MessageAcceptor acceptor = v.getValue();
                template.convertAndSendToUser(
                        v.getKey(), "/topic/ticker",
                        tickerService.getTickers(acceptor)
                );
            } catch (Exception e) {
                log.error("pushAnonymous error", e);
            }
        }

        long ct = System.currentTimeMillis() - st;
        log.info("pushAnonymous exit: cost = {}ms", ct);
    }

}
