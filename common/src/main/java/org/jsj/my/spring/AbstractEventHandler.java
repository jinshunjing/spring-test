package org.jsj.my.spring;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.exception.BaseException;
import org.jsj.my.exception.InternalServerException;
import org.jsj.my.util.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Abstract Event Handler
 *
 * @author JSJ
 */
@Slf4j
public class AbstractEventHandler {

    /**
     * 事件监听 AsyncListener
     *
     * @param output
     */
    protected void exceptional(DeferredResult<?> output) {
        output.onTimeout(() -> {
            log.error("Timeout");
            output.setErrorResult(new InternalServerException());
        });
        output.onError((Throwable t) -> {
            log.error("Error", t);
            output.setErrorResult(new InternalServerException());
        });
    }

    protected ResponseEntity<String> handleResult(Object body) {
        String json = JsonUtil.objectToJSONString(BaseResponse.make(body));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

    protected ResponseEntity<String> handleException(BaseException e){
        String json = JsonUtil.objectToJSONString(BaseResponse.make(e.getCode(), e.getMsg()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        log.error(e.getClass().getName(), e);
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }
}
