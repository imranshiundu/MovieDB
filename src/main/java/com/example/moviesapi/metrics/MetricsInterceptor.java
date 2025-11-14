package com.example.moviesapi.metrics;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MetricsInterceptor implements HandlerInterceptor {

    private final ApiMetricsService metricsService;

    public MetricsInterceptor(ApiMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String endpoint = request.getRequestURI();
        // Don't track metrics endpoints themselves to avoid infinite recursion
        if (!endpoint.startsWith("/api/metrics")) {
            metricsService.recordApiCall(endpoint);
        }
        return true;
    }
}