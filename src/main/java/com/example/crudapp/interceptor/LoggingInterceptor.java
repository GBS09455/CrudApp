package com.example.crudapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTR = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTR, startTime);
        log.info("REQUEST::  {} {} |  IP: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        log.debug("POST-HANDLE:: {} {} | status: {}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus());
    }


    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        long duration = (startTime != null) ? System.currentTimeMillis() - startTime : -1;
        log.info("RESPONSE {} {} | status: {} | duration: {} ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration);
        if (ex != null) {
            log.error("EXCEPTION {} {} | error: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        }
    }
}
