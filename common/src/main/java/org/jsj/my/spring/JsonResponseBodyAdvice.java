package org.jsj.my.spring;

import org.jsj.my.util.JsonUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * JSON response body advice
 *
 * @author Flow developers
 */
@ControllerAdvice(basePackages = "com.funchain.dapp.controller")
public class JsonResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override public boolean supports(MethodParameter returnType,
                                      Class<? extends HttpMessageConverter<?>> converterType) {
        RequestMapping anno = returnType.getMethodAnnotation(RequestMapping.class);
        if(anno != null) {
            String[] produces = anno.produces();
            if(produces != null && produces.length > 0) {
                return false;
            }
        }
        return true;
    }

    @Override public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                            ServerHttpResponse response) {
        if(MediaType.APPLICATION_OCTET_STREAM.equals(response.getHeaders().getContentType())) {
            return body;
        }

        BaseResponse resp = BaseResponse.make(body);

        if(selectedConverterType != MappingJackson2HttpMessageConverter.class) {
            ((ServletServerHttpResponse)response).getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            return JsonUtil.objectToJSONString(resp);
        }
        return resp;
    }
}
