package org.jsj.my.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsj.my.util.CallStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 安全校验
 * 同时支持地址和UID
 *
 * @author JSJ
 */
@Slf4j
public class V4AuthInterceptor implements HandlerInterceptor {
    private static final String OPTIONS = "OPTIONS";
    private static final String TOKEN_HEADER = "token";
    private static final String SESSION_HEADER = "session_id";
    private static final String AUTHORIZATION = "authorization";
    private static final String BEARER = "bearer";

    @Autowired
    private V4AuthService authService;

    /**
     * 不用token也能访问的URL
     */
    private List<String> ignoredUrl;
    public void addIgnoredUrl(String url) {
        if (Objects.isNull(ignoredUrl)) {
            ignoredUrl = new ArrayList<>();
        }
        ignoredUrl.add(url);
    }

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
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.isNotEmpty(token)) {
            token = StringUtils.trimToNull(token.substring(BEARER.length() + 1));
        }
        else {
            token = request.getHeader(TOKEN_HEADER);
            if (StringUtils.isEmpty(token)) {
                token = request.getHeader(SESSION_HEADER);
            }
        }

        // 验证token
        boolean passed = false;
        if (StringUtils.isNotEmpty(token)) {
            String at = "@";
            String addr = authService.getAddr(token);
            if (StringUtils.isNotEmpty(addr)) {
                String[] parts = StringUtils.split(addr, at);
                request.setAttribute("addr", parts[0]);
                passed = true;
            }
            String uid = authService.getUid(token);
            if (StringUtils.isNotEmpty(uid)) {
                String[] parts = StringUtils.split(uid, at);
                request.setAttribute("uid", new Long(parts[0]));
                passed = true;
            }
        }
        // FIXME 没有token也能访问的URL
        else {
            for (String prefix : ignoredUrl) {
                passed = url.startsWith(prefix);
                if (passed) {
                    break;
                }
            }
        }

        if (!passed) {
            log.error("Token expired: {}", url);
            response.sendError(403);
            return false;
        }

        // FIXME: 统计
        request.setAttribute("st", System.currentTimeMillis());
        request.setAttribute("uri", url);
        CallStats.getInstance().enter(url);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        // FIXME: 统计
        String url = (String) request.getAttribute("uri");
        Long st = (Long) request.getAttribute("st");
        if (null == url || null == st) {
            return;
        }
        CallStats.getInstance().exit(url, System.currentTimeMillis() - st);
    }

}
