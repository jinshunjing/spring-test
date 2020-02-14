package org.jsj.my.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 调用统计
 *
 * @author JSJ
 */
@Slf4j
public class CallStats {
    /**
     * 统计调用的次数
     */
    private Map<String, AtomicInteger> countStats;
    private static final int COUNT_SKIP = 5;

    /**
     * 统计超时的次数
     */
    private Map<String, AtomicInteger> costStats;
    private static final long COST_THRESHOLD_MS = 200L;
    private static final int COST_SKIP = 3;

    private static CallStats INSTANCE = new CallStats();
    public static CallStats getInstance() {
        return INSTANCE;
    }

    private CallStats() {
        countStats = new HashMap<>(64);
        costStats = new HashMap<>(64);
    }

    public void enter(String key) {
        // 计数
        AtomicInteger count = countStats.get(key);
        if (Objects.isNull(count)) {
            count = new AtomicInteger(0);
            countStats.put(key, count);
        }
        int k = count.incrementAndGet();
        if (k % COUNT_SKIP == 0) {
            log.info("CALL STATS: {}, {}", key, k);
        }
    }

    public void exit(String key, long cost) {
        // 计时
        if (cost > COST_THRESHOLD_MS) {
            AtomicInteger count = costStats.get(key);
            if (Objects.isNull(count)) {
                count = new AtomicInteger(0);
                costStats.put(key, count);
            }
            int k = count.incrementAndGet();
            if (k % COST_SKIP == 0) {
                log.info("COST STATS: {}, {}, {}ms", key, k, cost);
            }
        }
    }

    public int count(String key) {
        AtomicInteger count = countStats.get(key);
        if (Objects.isNull(count)) {
            return -1;
        } else {
            return count.get();
        }
    }

    public void print() {
        log.info("CALL STATS: {}", JSON.toJSONString(countStats));
        log.info("COST STATS: {}", JSON.toJSONString(costStats));
    }
}
