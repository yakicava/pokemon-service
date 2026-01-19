package com.app.pokemon.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (handler instanceof HandlerMethod hm) {
            String controller = hm.getBeanType().getSimpleName();
            String handlerMethod = hm.getMethod().getName();
            log.info("[HANDLER:PRE ] {} {} -> {}#{}",
                    method, uri, controller, handlerMethod);
        } else {
            log.info("[HANDLER:PRE ] {} {} -> {}",
                    method, uri, handler.getClass().getSimpleName());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        String uri = request.getRequestURI();
        int status = response.getStatus();

        if (ex != null) {
            log.warn("[HANDLER:END ] status={} uri={} ex={}",
                    status, uri, ex.toString(), ex);
        } else {
            log.info("[HANDLER:END ] status={} uri={}", status, uri);
        }
    }
}
