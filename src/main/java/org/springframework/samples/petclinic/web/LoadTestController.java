package org.springframework.samples.petclinic.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auto Scaling 시연을 위한 부하테스트 전용 엔드포인트.
 * durationMs 동안 순수 CPU 연산만 반복해 WAS CPU 사용률을 끌어올린다.
 */
@RestController
public class LoadTestController {

    @GetMapping("/loadtest/cpu")
    public String cpuLoad(@RequestParam(defaultValue = "500") long durationMs) {
        long capped = Math.min(durationMs, 5000);
        long end = System.currentTimeMillis() + capped;
        double x = 0;
        while (System.currentTimeMillis() < end) {
            x += Math.sqrt(Math.random());
        }
        return "busy " + capped + "ms, result=" + x;
    }
}
