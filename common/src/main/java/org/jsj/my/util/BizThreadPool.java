package org.jsj.my.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 业务线程池
 *
 * @author JSJ
 */
@Component
public class BizThreadPool {

    @Value("${server.biz_thread_pool.core:20}")
    private int corePoolSize;

    @Value("${server.biz_thread_pool.max:100}")
    private int maximumPoolSize;

    @Value("${server.biz_thread_pool.idle:60000}")
    private long keepAliveTime;

    @Value("${server.biz_thread_pool.queue:20}")
    private int queueSize;

    private ExecutorService executor;

    public ExecutorService getExecutor() {
        if (null == executor) {
            // 同步确保只创建了一个executor实例
            synchronized (this) {
                if (null == executor) {
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(queueSize);
                    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("biz-worker-%d").build();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
                    executor = new ThreadPoolExecutor(
                            corePoolSize, maximumPoolSize,
                            keepAliveTime, TimeUnit.MILLISECONDS,
                            workQueue,
                            threadFactory,
                            handler);
                }
            }
        }
        return executor;
    }
}
