package org.jsj.my.aspect;

import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jsj.my.annotation.HttpRetryer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 失败重试，通过Guava
 *
 * @author JSJ
 */
@Slf4j
@Aspect
@Component
public class HttpRetryerAspect {
    @Around(value = "@annotation(httpRetryerAnnotation)")
    public Object around(ProceedingJoinPoint pjp, HttpRetryer httpRetryerAnnotation) throws Throwable {
        if (httpRetryerAnnotation.attemptNumber() <= 1) {
            return pjp.proceed();
        }

        RetryerBuilder retryer = RetryerBuilder.newBuilder();
        int attemptNumber = httpRetryerAnnotation.attemptNumber();
        if (attemptNumber > 1) {
            retryer.withStopStrategy(StopStrategies.stopAfterAttempt(attemptNumber));
        }

        int intervalSeconds = httpRetryerAnnotation.intervalSeconds();
        if (intervalSeconds > 0) {
            retryer.withWaitStrategy(WaitStrategies.fixedWait(intervalSeconds, TimeUnit.SECONDS));
        }


        Class [] throwables = httpRetryerAnnotation.retryThrowable();
        if (!CollectionUtils.isEmpty(Arrays.asList(throwables))) {
            for (Class throwable : throwables) {
                if (Objects.nonNull(throwable) && Throwable.class.isAssignableFrom(throwable)) {
                    retryer.retryIfExceptionOfType(throwable);
                }
            }
        } else {
            retryer.retryIfException();
        }

        return retryer.build().call(() -> {
            try {
                return pjp.proceed();
            } catch (Throwable t) {
                if (t instanceof Exception) {
                    throw (Exception) t;
                } else {
                    throw new Exception(t);
                }
            }
        });
    }
}
