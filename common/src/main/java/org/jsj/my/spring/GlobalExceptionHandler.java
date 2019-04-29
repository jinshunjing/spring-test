package org.jsj.my.spring;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.constant.ErrorCode;
import org.jsj.my.exception.BaseException;
import org.jsj.my.util.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常处理
 *
 * @author JSJ
 */
@ControllerAdvice(basePackages = "org.jsj.my.controller")
@RestController
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获业务异常
     *auth
     * @param e 异常信息
     * @return 返回标准ResponseEntity
     */
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException e){
        String json = JsonUtil.objectToJSONString(BaseResponse.make(e.getCode(), e.getMsg()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        log.error(e.getClass().getName(), e);
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

    /**
     * 捕获所有异常
     * 统一返回ErrorCode.SERVER_ERROR给客户端
     *
     * @param e 异常信息
     * @return 返回标准ResponseEntity
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e){
        String json = JsonUtil.objectToJSONString(BaseResponse.make(ErrorCode.SERVER_ERROR, "Internal Server Error"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        log.error(e.getClass().getName(), e);
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }
}
