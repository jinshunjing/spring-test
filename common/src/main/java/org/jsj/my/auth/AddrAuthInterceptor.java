package org.jsj.my.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全校验
 *
 * @author JSJ
 */
@Slf4j
public class AddrAuthInterceptor implements HandlerInterceptor {
    private static final String OPTIONS = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String url = request.getRequestURI();

        if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String doubleSlash = "//";
        if (url.startsWith(doubleSlash)) {
            url = url.substring(1);
        }

        // exception handling
        String error = "/error";
        if (url.startsWith(error)) {
            return true;
        }

        // swagger
        String v2 = "/v2";
        String swagger = "/swagger";
        String webjars = "/webjars/";
        if (url.startsWith(v2) || url.startsWith(swagger) || url.startsWith(webjars)) {
            return true;
        }

        // 获取请求中带上的token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            Cookie cookie = WebUtils.getCookie(request, "token");
            if (cookie != null) {
                token = cookie.getValue();
            }
        }
        if (StringUtils.isEmpty(token)) {
            log.error("No token: {}", url);
            response.sendError(403);
            return false;
        }

        request.setAttribute("addr", token);

        // FIXME
        request.setAttribute("st", System.currentTimeMillis());
        request.setAttribute("uri", url);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        // FIXME
        Long st = (Long) request.getAttribute("st");
        if (null == st) {
            return;
        }

        long threshold = 200;
        long ct = System.currentTimeMillis() - st;
        if (threshold > ct) {
            return;
        }

        String url = (String) request.getAttribute("uri");
        String uid = (String) request.getAttribute("addr");

        log.info("{}@{}: {}ms", url, uid, ct);
    }

}
