package org.jsj.my.config;

import com.google.common.eventbus.EventBus;
import org.jsj.my.gov.TxGovernor;
import org.jsj.my.ops.TxOps;
import org.jsj.my.service.TxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 事件注册中心
 *
 * @author JSJ
 */
@Configuration
public class EventHandlerConfig {
    @Autowired
    private EventBus eventBus;

    @Bean
    public TxGovernor txGovernor() {
        TxGovernor governor = new TxGovernor();
        eventBus.register(governor);
        return governor;
    }

    @Bean
    public TxService txService() {
        return new TxService();
    }

    @Bean
    public TxOps txOps() {
        return new TxOps();
    }
}
