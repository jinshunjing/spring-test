package org.jsj.my.schedule;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.consumer.TxConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 各种维护作业
 *
 * @author JSJ
 */
@Profile("!dev")
@Slf4j
@Component
public class MaintenanceJob {
    @Autowired
    private TxConsumer txConsumer;

    @Scheduled(fixedRate = 3600_000L, initialDelay = 4_000L)
    public void hourTask() {
        startRocketConsumer();
    }

    /**
     * 启动Rocket消费者
     */
    private void startRocketConsumer() {
        log.info("[JOB] Start Rocket Consumer");
        txConsumer.start();
    }

}
