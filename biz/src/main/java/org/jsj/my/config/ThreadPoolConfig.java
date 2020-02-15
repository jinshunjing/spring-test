package org.jsj.my.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * Thread pool configurations
 *
 * @author JSJ
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    @Value("${server.biz_thread_pool.core:20}")
    private int corePoolSize;

    @Value("${server.biz_thread_pool.max:200}")
    private int maximumPoolSize;

    @Value("${server.biz_thread_pool.idle:60000}")
    private long keepAliveTime;

    @Value("${server.biz_thread_pool.queue:25}")
    private int queueSize;

    /**
     * 业务线程池
     *
     * @return
     */
    @Bean("bizThreadPool")
    public ExecutorService bizThreadPool() {
        log.info("=== To create BizThreadPool");
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(queueSize);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("biz-worker-%d").build();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ExecutorService executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS,
                workQueue,
                threadFactory,
                handler);
        log.info("=== Created BizThreadPool");
        return executor;
    }

    /**
     * Spring 使用的线程池
     *
     * @return
     */
    @Bean("commonThreadPool")
    public ThreadPoolTaskExecutor commonThreadPool() {
        log.info("=== To create CommonThreadPool");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("spring-worker-");
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(100);
        executor.setKeepAliveSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setAllowCoreThreadTimeOut(true);
        log.info("=== Created CommonThreadPool");
        return executor;
    }

    /**
     * Event Bus
     *
     * @return
     */
    @Bean("eventBus")
    public EventBus eventBus() {
        log.info("=== To create EventBus");
        EventBus bus = new AsyncEventBus("event-bus", bizThreadPool());
        log.info("=== Created EventBus");
        return bus;
    }
}
