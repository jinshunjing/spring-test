package org.jsj.my.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 失败重试的注解
 *
 * @author JSJ
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRetryer {
    int intervalSeconds() default 3;
    int attemptNumber() default 10;
    Class[] retryThrowable() default {};
}
