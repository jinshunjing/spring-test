package org.jsj.my.controller;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.exception.BaseException;
import org.jsj.my.exception.InternalServerException;
import org.jsj.my.exception.TimeoutException;
import org.jsj.my.spring.BaseResponse;
import org.jsj.my.util.BizThreadPool;
import org.jsj.my.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Servlet 3.0 Async
 *
 * @author JSJ
 */
@Slf4j
public abstract class AbstractAsyncController {
    public static final Long TIMEOUT = 2000L;

    @Autowired
    protected BizThreadPool bizThreadPool;

    /**
     * 事件监听 AsyncListener
     * 注意：在IO线程池里处理
     *
     * @param output
     */
    protected void deferredCallbacks(DeferredResult<?> output) {
        output.onCompletion(() -> {
            log.info("=== [IO] completed");
        });

        output.onTimeout(() -> {
            log.error("=== [IO] timeout");
            output.setErrorResult(handleException(new TimeoutException()));
        });

        output.onError((Throwable t) -> {
            log.error("=== [IO] error", t);
            output.setErrorResult(handleException(new InternalServerException()));
        });
    }

    private ResponseEntity<Object> handleException(BaseException e){
        String json = JsonUtil.objectToJSONString(BaseResponse.make(e.getCode(), e.getMsg()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        log.error(e.getClass().getName(), e);
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

}
