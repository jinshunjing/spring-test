package org.jsj.my.sharding;

/**
 * 分库分表主键
 *
 * @author JSJ
 */
public class MyKeyGenerator {
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_MASK = 4095L;
    private static final long SEQUENCE_SKIP = 5L;
    private static final long WORKER_ID_LEFT_SHIFT_BITS = 12L;
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = 22L;
    private static final long WORKER_ID_MAX_VALUE = 1024L;
    private static final long EPOCH = 1477929600000L;
    private long workerId;
    private long sequence;
    private long lastTime;

    private static MyKeyGenerator INSTANCE = new MyKeyGenerator();
    public static MyKeyGenerator getInstance() {
        return INSTANCE;
    }

    private MyKeyGenerator() {
        this.workerId = (long) (Math.random() * WORKER_ID_MAX_VALUE);
    }

    public void setWorkerId(long worker) {
        if(worker >= 0L && worker < WORKER_ID_MAX_VALUE) {
            workerId = worker;
        }
    }

    public synchronized Number generateKey() {
        long currentMillis = System.currentTimeMillis();
        if(this.lastTime > currentMillis) {
            throw new IllegalStateException("Clock is moving backwards!");
        }
        if (this.lastTime == currentMillis) {
            if (0L == (this.sequence = (this.sequence + SEQUENCE_SKIP & SEQUENCE_MASK))) {
                currentMillis = this.waitUntilNextTime(currentMillis);
            }
        } else {
            this.sequence = 0L;
        }
        this.lastTime = currentMillis;
        return currentMillis - EPOCH << TIMESTAMP_LEFT_SHIFT_BITS | workerId << WORKER_ID_LEFT_SHIFT_BITS | this.sequence;
    }

    private long waitUntilNextTime(long lastTime) {
        long result = System.currentTimeMillis();
        while (result <= lastTime) {
            result = System.currentTimeMillis();
        }
        return result;
    }

}
