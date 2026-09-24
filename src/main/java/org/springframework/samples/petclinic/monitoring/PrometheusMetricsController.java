package org.springframework.samples.petclinic.monitoring;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrometheusMetricsController {

    private static final String PROMETHEUS_CONTENT_TYPE =
            "text/plain; version=0.0.4; charset=utf-8";

    private final PrometheusMeterRegistry registry;

    public PrometheusMetricsController(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }

    @GetMapping(
            value = "/actuator/prometheus",
            produces = PROMETHEUS_CONTENT_TYPE
    )
    public String prometheus() {
        return registry.scrape();
    }
}
