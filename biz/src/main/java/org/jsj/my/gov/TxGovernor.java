package org.jsj.my.gov;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.jsj.my.event.TxEvent;
import org.jsj.my.service.TxService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 异步处理
 *
 * @author JSJ
 */
@Slf4j
public class TxGovernor extends AbstractGovernor {
    @Autowired
    private TxService txService;

    /**
     * 处理业务逻辑
     *
     * @param event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void process(TxEvent event) {
        // TODO 幂等性一：查询消息的处理状态

        // TODO 幂等性二：分布式锁
        String lockKey = "lock.process" + event.getTxid();
        String lockValue = System.currentTimeMillis() + "";
        if(!redisLock.lock(lockKey, event.getTxid())) {
            log.info("confirm open is locked: {}", lockKey);
            return;
        }

        try {
            // TODO 处理业务
            txService.process(event);

        } catch (Exception e) {
            log.error("process error", e);
            dingTalkNotify.notify(e);
        } finally {
            redisLock.unlock(lockKey, lockValue);
        }
    }

}
