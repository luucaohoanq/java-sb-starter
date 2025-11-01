/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.performance;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for performance testing endpoints. Provides various endpoints to test different
 * performance scenarios.
 */
@RestController
@RequestMapping("${api.prefix}/performance")
@RequiredArgsConstructor
@Tag(name = "🚀 performance", description = "Performance testing endpoints")
@Slf4j
public class PerformanceTestController {

    private final MeterRegistry meterRegistry;
    private final Random random = new Random();

    @GetMapping("/fast")
    @Timed(value = "performance.fast", description = "Fast response endpoint")
    @Operation(
            summary = "Fast endpoint",
            description = "Returns immediately - good for baseline testing")
    public ResponseEntity<Map<String, Object>> fastEndpoint() {
        long startTime = System.nanoTime();

        Counter.builder("performance.fast.calls")
                .description("Number of calls to fast endpoint")
                .register(meterRegistry)
                .increment();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Fast response");
        response.put("timestamp", System.currentTimeMillis());

        long responseTimeNs = System.nanoTime() - startTime;
        double responseTimeMs = responseTimeNs / 1_000_000.0;
        response.put(
                "responseTime",
                Math.round(responseTimeMs * 100.0) / 100.0); // Round to 2 decimal places

        return ResponseEntity.ok(response);
    }

    @GetMapping("/slow")
    @Timed(value = "performance.slow", description = "Slow response endpoint")
    @Operation(summary = "Slow endpoint", description = "Simulates slow database/API calls")
    public ResponseEntity<Map<String, Object>> slowEndpoint(
            @RequestParam(defaultValue = "500") int delayMs) {

        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // Simulate slow operation
            Thread.sleep(delayMs);

            Counter.builder("performance.slow.calls")
                    .description("Number of calls to slow endpoint")
                    .register(meterRegistry)
                    .increment();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Slow response completed");
            response.put("delayMs", delayMs);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().build();
        } finally {
            sample.stop(
                    Timer.builder("performance.slow.timer")
                            .description("Timer for slow endpoint")
                            .register(meterRegistry));
        }
    }

    @GetMapping("/variable/{complexity}")
    @Timed(value = "performance.variable", description = "Variable complexity endpoint")
    @Operation(summary = "Variable complexity", description = "Simulates different CPU loads")
    public ResponseEntity<Map<String, Object>> variableComplexity(@PathVariable int complexity) {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // Simulate CPU intensive work
            long result = 0;
            for (int i = 0; i < complexity * 10000; i++) {
                result += Math.sqrt(i) * Math.sin(i);
            }

            Counter.builder("performance.variable.calls")
                    .tag("complexity", String.valueOf(complexity))
                    .register(meterRegistry)
                    .increment();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Variable complexity completed");
            response.put("complexity", complexity);
            response.put("result", result);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } finally {
            sample.stop(
                    Timer.builder("performance.variable.timer")
                            .tag("complexity", String.valueOf(complexity))
                            .register(meterRegistry));
        }
    }

    @GetMapping("/random")
    @Timed(value = "performance.random", description = "Random response time endpoint")
    @Operation(
            summary = "Random response time",
            description = "Simulates unpredictable response times")
    public ResponseEntity<Map<String, Object>> randomResponseTime() {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // Random delay between 50-500ms
            int delay = 50 + random.nextInt(450);
            Thread.sleep(delay);

            Counter.builder("performance.random.calls").register(meterRegistry).increment();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Random response completed");
            response.put("actualDelay", delay);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().build();
        } finally {
            sample.stop(Timer.builder("performance.random.timer").register(meterRegistry));
        }
    }

    @PostMapping("/data")
    @Timed(value = "performance.data", description = "Data processing endpoint")
    @Operation(
            summary = "Data processing",
            description = "Processes POST data - tests payload handling")
    public ResponseEntity<Map<String, Object>> processData(@RequestBody Map<String, Object> data) {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // Simulate data processing
            int processingTime = Math.min(data.size() * 10, 1000); // Max 1 second
            Thread.sleep(processingTime);

            Counter.builder("performance.data.calls")
                    .tag("dataSize", String.valueOf(data.size()))
                    .register(meterRegistry)
                    .increment();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Data processed successfully");
            response.put("inputSize", data.size());
            response.put("processingTime", processingTime);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().build();
        } finally {
            sample.stop(Timer.builder("performance.data.timer").register(meterRegistry));
        }
    }

    @GetMapping("/memory")
    @Timed(value = "performance.memory", description = "Memory usage endpoint")
    @Operation(summary = "Memory usage", description = "Returns current memory statistics")
    public ResponseEntity<Map<String, Object>> memoryStats() {
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> memoryInfo = new HashMap<>();
        memoryInfo.put("totalMemory", runtime.totalMemory());
        memoryInfo.put("freeMemory", runtime.freeMemory());
        memoryInfo.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        memoryInfo.put("maxMemory", runtime.maxMemory());
        memoryInfo.put("timestamp", System.currentTimeMillis());

        Counter.builder("performance.memory.calls").register(meterRegistry).increment();

        return ResponseEntity.ok(memoryInfo);
    }

    @GetMapping("/metrics")
    @Operation(summary = "Performance metrics", description = "Returns custom performance metrics")
    public ResponseEntity<Map<String, Object>> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // Get custom counters
        meterRegistry
                .getMeters()
                .forEach(
                        meter -> {
                            if (meter.getId().getName().startsWith("performance.")) {
                                if (meter instanceof Counter counter) {
                                    metrics.put(meter.getId().getName(), counter.count());
                                } else if (meter instanceof Timer timer) {
                                    metrics.put(
                                            meter.getId().getName() + ".mean",
                                            timer.mean(TimeUnit.MILLISECONDS));
                                    metrics.put(
                                            meter.getId().getName() + ".max",
                                            timer.max(TimeUnit.MILLISECONDS));
                                    metrics.put(meter.getId().getName() + ".count", timer.count());
                                }
                            }
                        });

        return ResponseEntity.ok(metrics);
    }
}
