package org.jsj.my.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jsj.my.exception.BaseException;
import org.jsj.my.exception.DatabaseException;
import org.jsj.my.exception.InternalServerException;
import org.jsj.my.exception.TimeoutException;
import org.jsj.my.model.StringResponse;
import org.jsj.my.spring.BaseResponse;
import org.jsj.my.util.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncManager;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ForkJoinPool;

/**
 * Servlet 3.0 Async
 * - IO线程立即返回，提交给业务线程处理
 * - 业务线程处理完，IO线程会生成新的request，但是response还是原来的那个对象
 *
 * curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8666/my/public/async/normal'
 * curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8666/my/public/async/timeout'
 * curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8666/my/public/async/error'
 *
 * @author JSJ
 */
@Slf4j
@RestController("AsyncTestCtrl")
@RequestMapping("/my/public")
@Api(description = "0.1.0 API", tags = {"Public"})
public class AsyncTestController extends AbstractAsyncController {


    @PostMapping("/async/normal")
    @ApiOperation(value = "非阻塞",
            notes = "Execute in thread pool")
    public DeferredResult<StringResponse> asyncNormal(HttpServletRequest request) {
        log.info("=== [IO] async entrance");
        DeferredResult<StringResponse> output = new DeferredResult<>(TIMEOUT);
        deferredCallbacks(output);

        /**
         * 在业务线程池处理
         */
        bizThreadPool.getExecutor().submit(() -> {
        //ForkJoinPool.commonPool().submit(() -> {
            log.info("=== [BU] entrance");

            // 模拟业务逻辑
            try {
                Thread.sleep(200L);
            } catch (Exception e) {
                log.error("Error", e);
            }

            // 正常返回
            output.setResult(StringResponse.builder().value("0.2.0").build());
            log.info("=== [BU] exit");
        });

        log.info("=== [IO] async exit");
        return output;
    }

    @PostMapping("/async/timeout")
    @ApiOperation(value = "非阻塞",
            notes = "Execute in thread pool")
    public DeferredResult<StringResponse> asyncTimeout(HttpServletRequest request) {
        log.info("=== [IO] async entrance");
        DeferredResult<StringResponse> output = new DeferredResult<>(TIMEOUT);
        deferredCallbacks(output);

        /**
         * 在业务线程池处理
         */
        ForkJoinPool.commonPool().submit(() -> {
            log.info("=== [BU] entrance");

            // 模拟业务逻辑
            try {
                Thread.sleep(5000L);
            } catch (Exception e) {
                log.error("Error", e);
            }

            // 抛出异常
            output.setErrorResult(new DatabaseException());

            // 正常返回
            output.setResult(StringResponse.builder().value("0.2.0").build());
            log.info("=== [BU] exit");
        });

        log.info("=== [IO] async exit");
        return output;
    }

    @PostMapping("/async/error")
    @ApiOperation(value = "非阻塞",
            notes = "Execute in thread pool")
    public DeferredResult<StringResponse> asyncError(HttpServletRequest request) {
        log.info("=== [IO] async entrance");
        DeferredResult<StringResponse> output = new DeferredResult<>(TIMEOUT);
        deferredCallbacks(output);

        /**
         * 在业务线程池处理
         */
        ForkJoinPool.commonPool().submit(() -> {
            log.info("=== [BU] entrance");

            // 模拟业务逻辑
            try {
                Thread.sleep(100L);
            } catch (Exception e) {
                log.error("Error", e);
            }

            // 抛出异常
            output.setErrorResult(new DatabaseException());

            log.info("=== [BU] exit");
        });

        log.info("=== [IO] async exit");
        return output;
    }

}
