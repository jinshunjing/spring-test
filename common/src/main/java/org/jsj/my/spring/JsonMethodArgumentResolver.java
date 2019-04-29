package org.jsj.my.spring;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jsj.my.util.Utils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * JSON method argument resolver
 *
 * @author Flow developers
 */
public class JsonMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String MODEL_BASE_PACKAGE = Utils.getParentPackage(
            Utils.getParentPackage(JsonMethodArgumentResolver.class.getPackage()));

    @Override public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().getPackage().getName().startsWith(MODEL_BASE_PACKAGE);
    }

    @Override public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String json = webRequest.getParameter(parameter.getParameterName());
        if(StringUtils.isEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, parameter.getParameterType());
    }
}
