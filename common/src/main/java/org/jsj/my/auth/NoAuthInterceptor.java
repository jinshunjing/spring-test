package org.jsj.my.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
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
public class NoAuthInterceptor extends HandlerInterceptorAdapter {
    private static final String OPTIONS = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler)
            throws Exception {
        log.info("NoAuthInterceptor#preHandle entrance: {}, {}", request.hashCode(), response.hashCode());
        String url = request.getRequestURI();

        request.setAttribute("st", System.currentTimeMillis());
        request.setAttribute("uri", url);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, @Nullable ModelAndView modelAndView)
            throws Exception {
        log.info("NoAuthInterceptor#postHandle entrance: {}, {}", request.getAttribute("st"), response.hashCode());
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response,
                                               Object handler)
            throws Exception {
        log.info("NoAuthInterceptor#afterConcurrentHandlingStarted entrance: {}, {}", request.getAttribute("st"), response.hashCode());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        log.info("NoAuthInterceptor#afterCompletion entrance: {}, {}", request.getAttribute("st"), response.hashCode());
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

        log.info("{}: {}ms", url, ct);
    }
}
