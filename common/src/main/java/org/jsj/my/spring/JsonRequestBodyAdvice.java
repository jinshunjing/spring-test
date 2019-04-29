package org.jsj.my.spring;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;

/**
 * JSON request body advice
 *
 * @author Flow developers
 */
@ControllerAdvice(basePackages = "com.funchain.dapp.controller")
public class JsonRequestBodyAdvice implements RequestBodyAdvice {
    @Override public boolean supports(MethodParameter methodParameter, Type targetType,
                                      Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    @Override public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }

    @Override public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                                     Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }

    @Override public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                          Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }
}
