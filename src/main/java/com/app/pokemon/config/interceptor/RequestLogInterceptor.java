package com.app.pokemon.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String requestId = (String) request.getAttribute("requestId");
        if (requestId == null) {
            log.warn("[INTERCEPTOR:PRE ] requestId is missing uri={}", request.getRequestURI());
        } else {
            log.info("[INTERCEPTOR:PRE ] requestId={} uri={}", requestId, request.getRequestURI());
        }
        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        String requestId = (String) request.getAttribute("requestId");

        if (requestId == null) {
            log.warn("[INTERCEPTOR:POST] requestId is missing uri={}", request.getRequestURI());
        } else {
            log.info("[INTERCEPTOR:POST] requestId={} uri={}", requestId, request.getRequestURI());
        }
    }

}
