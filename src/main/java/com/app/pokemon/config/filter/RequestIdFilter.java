package com.app.pokemon.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestIdFilter.class);

    private static final String MDC_KEY = "requestId";
    private static final String HEADER = "X-Request-Id";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !(uri.startsWith("/pokedex") || uri.startsWith("/type-battle"));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String incoming = request.getHeader(HEADER);
        String requestId = (incoming != null && !incoming.isBlank())
                ? incoming
                : UUID.randomUUID().toString();

        long start = System.currentTimeMillis();

        MDC.put(MDC_KEY, requestId);

        response.setHeader(HEADER, requestId);

        String uri = request.getRequestURI();
        String qs = request.getQueryString();
        String fullPath = (qs == null ? uri : uri + "?" + qs);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long ms = System.currentTimeMillis() - start;

            log.info("[ACCESS ] method={} path={} status={} time={}ms",
                    request.getMethod(), fullPath, response.getStatus(), ms);

            MDC.remove(MDC_KEY);
        }
    }
}
