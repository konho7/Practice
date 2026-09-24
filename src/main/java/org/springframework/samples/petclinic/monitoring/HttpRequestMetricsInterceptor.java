package org.springframework.samples.petclinic.monitoring;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Component
public class HttpRequestMetricsInterceptor implements HandlerInterceptor {

    private static final String START_NANOS_ATTRIBUTE =
            HttpRequestMetricsInterceptor.class.getName() + ".startNanos";

    private final MeterRegistry registry;

    public HttpRequestMetricsInterceptor(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        request.setAttribute(START_NANOS_ATTRIBUTE, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {

        Object startValue = request.getAttribute(START_NANOS_ATTRIBUTE);
        if (!(startValue instanceof Long)) {
            return;
        }

        Object patternValue = request.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE
        );

        String uriPattern = patternValue == null
                ? "UNKNOWN"
                : patternValue.toString();

        String status = Integer.toString(response.getStatus());
        String outcome = outcome(response.getStatus());
        long durationNanos = System.nanoTime() - (Long) startValue;

        Timer.builder("http.server.requests")
                .description("PetClinic Spring MVC request duration")
                .tag("method", request.getMethod())
                .tag("uri", uriPattern)
                .tag("status", status)
                .tag("outcome", outcome)
                .register(registry)
                .record(durationNanos, TimeUnit.NANOSECONDS);
    }

    private String outcome(int status) {
        if (status >= 100 && status < 200) return "INFORMATIONAL";
        if (status < 300) return "SUCCESS";
        if (status < 400) return "REDIRECTION";
        if (status < 500) return "CLIENT_ERROR";
        return "SERVER_ERROR";
    }
}
