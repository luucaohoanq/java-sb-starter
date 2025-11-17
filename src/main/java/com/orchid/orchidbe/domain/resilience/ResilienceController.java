/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.resilience;

import com.orchid.orchidbe.apis.MyApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller demonstrating Resilience4j patterns.
 * Provides endpoints to test Circuit Breaker, Retry, Rate Limiter, Bulkhead, and Time Limiter.
 *
 * <p>Access Zipkin UI at: http://localhost:9411
 * Access Resilience4j metrics at: http://localhost:8080/actuator/circuitbreakers
 * Access health indicators at: http://localhost:8080/actuator/health
 */
@RestController
@RequestMapping("${api.prefix}/resilience")
@RequiredArgsConstructor
@Tag(name = "🛡️ resilience", description = "Resilience4j pattern demonstrations and distributed tracing")
@Slf4j
public class ResilienceController {

    private final ResilientService resilientService;

    @GetMapping("/circuit-breaker")
    @Operation(
            summary = "Circuit Breaker Pattern",
            description = """
                    Demonstrates Circuit Breaker pattern that prevents cascading failures.
                    The circuit opens after 50% failure rate in a sliding window of 10 calls.
                    Try calling with shouldFail=true multiple times to open the circuit.

                    **Circuit States:**
                    - CLOSED: Normal operation
                    - OPEN: Circuit is open, calls fail immediately
                    - HALF_OPEN: Testing if service recovered
                    """)
    @ApiResponse(responseCode = "200", description = "Success or fallback response")
    public ResponseEntity<MyApiResponse<String>> testCircuitBreaker(
            @Parameter(description = "Should the call fail?")
            @RequestParam(defaultValue = "false") boolean shouldFail) {

        log.info("Circuit breaker test requested with shouldFail={}", shouldFail);
        String result = resilientService.callExternalService(shouldFail);
        return MyApiResponse.success("Circuit breaker pattern executed, data: " + result);
    }

    @GetMapping("/retry")
    @Operation(
            summary = "Retry Pattern",
            description = """
                    Demonstrates Retry pattern with exponential backoff.
                    Will retry up to 3 times with increasing wait duration (1s, 2s, 4s).
                    Watch the logs to see retry attempts.
                    """)
    @ApiResponse(responseCode = "200", description = "Success after retries or fallback")
    public ResponseEntity<MyApiResponse<String>> testRetry(
            @Parameter(description = "Should the call fail?")
            @RequestParam(defaultValue = "false") boolean shouldFail) {

        log.info("Retry test requested with shouldFail={}", shouldFail);
        String result = resilientService.callWithRetry(shouldFail);
        return MyApiResponse.success("Retry pattern executed, data: " + result);
    }

    @GetMapping("/rate-limiter")
    @Operation(
            summary = "Rate Limiter Pattern",
            description = """
                    Demonstrates Rate Limiter pattern that controls request rate.
                    Allows 100 requests per second for the 'api' rate limiter.
                    Try calling this endpoint rapidly to trigger rate limiting.
                    """)
    @ApiResponse(responseCode = "200", description = "Success or rate limit exceeded")
    public ResponseEntity<MyApiResponse<String>> testRateLimiter() {

        log.debug("Rate limiter test requested");
        String result = resilientService.callWithRateLimit();
        return MyApiResponse.success("Rate limiter pattern executed, data: " + result);
    }

    @GetMapping("/bulkhead")
    @Operation(
            summary = "Bulkhead Pattern",
            description = """
                    Demonstrates Bulkhead pattern that limits concurrent executions.
                    Allows maximum 10 concurrent calls to prevent resource exhaustion.
                    Try making multiple concurrent requests to see bulkhead in action.
                    """)
    @ApiResponse(responseCode = "200", description = "Success or bulkhead capacity reached")
    public ResponseEntity<MyApiResponse<String>> testBulkhead() {

        log.info("Bulkhead test requested");
        String result = resilientService.callWithBulkhead();

        return MyApiResponse.success("Bulkhead pattern executed, data: " + result);
    }

    @GetMapping("/time-limiter")
    @Operation(
            summary = "Time Limiter Pattern",
            description = """
                    Demonstrates Time Limiter pattern that limits execution time.
                    Times out after 3 seconds for external service calls.
                    Try with delayMs > 3000 to trigger timeout.
                    """)
    @ApiResponse(responseCode = "200", description = "Success or timeout")
    public CompletableFuture<ResponseEntity<MyApiResponse<String>>> testTimeLimiter(
            @Parameter(description = "Delay in milliseconds")
            @RequestParam(defaultValue = "1000") long delayMs) {

        log.info("Time limiter test requested with delay={}ms", delayMs);

        return resilientService.callWithTimeLimit(delayMs)
                .thenApply(result -> MyApiResponse.success("Time limiter pattern executed"))
                .exceptionally(ex -> MyApiResponse.success("Time limiter pattern timed out or failed: " + ex.getMessage()));
    }

    @GetMapping("/combined")
    @Operation(
            summary = "Combined Patterns",
            description = """
                    Demonstrates multiple Resilience4j patterns working together:
                    - Circuit Breaker: Prevents cascading failures
                    - Retry: Retries failed operations
                    - Rate Limiter: Controls request rate

                    This shows how patterns can be composed for robust service calls.
                    """)
    @ApiResponse(responseCode = "200", description = "Success or fallback from any pattern")
    public ResponseEntity<MyApiResponse<String>> testCombined(
            @Parameter(description = "Should the call fail?")
            @RequestParam(defaultValue = "false") boolean shouldFail) {

        log.info("Combined patterns test requested with shouldFail={}", shouldFail);
        String result = resilientService.callWithCombinedPatterns(shouldFail);
        return MyApiResponse.success("Combined patterns executed, data: " + result);
    }

    @GetMapping("/database")
    @Operation(
            summary = "Database Resilience",
            description = """
                    Demonstrates resilience patterns for database operations.
                    Uses Circuit Breaker and Retry specific to database calls.
                    Database has different configuration with more retries and larger sliding window.
                    """)
    @ApiResponse(responseCode = "200", description = "Success or fallback")
    public ResponseEntity<MyApiResponse<String>> testDatabase(
            @Parameter(description = "Should the call fail?")
            @RequestParam(defaultValue = "false") boolean shouldFail) {

        log.info("Database resilience test requested with shouldFail={}", shouldFail);
        String result = resilientService.callDatabase(shouldFail);

        return MyApiResponse.success("Database resilience pattern executed, data: " + result);
    }

    @GetMapping("/info")
    @Operation(
            summary = "Resilience4j Information",
            description = """
                    Provides information about available Resilience4j endpoints and configuration.

                    **Useful Actuator Endpoints:**
                    - /actuator/health - Overall health including circuit breakers
                    - /actuator/circuitbreakers - All circuit breaker states
                    - /actuator/circuitbreakerevents - Recent circuit breaker events
                    - /actuator/retries - All retry instances
                    - /actuator/ratelimiters - All rate limiter instances
                    - /actuator/bulkheads - All bulkhead instances
                    - /actuator/metrics - Micrometer metrics including Resilience4j

                    **Zipkin Tracing:**
                    - Access Zipkin UI at http://localhost:9411 to see distributed traces
                    """)
    public ResponseEntity<MyApiResponse<Map<String, Object>>> getInfo() {
        Map<String, Object> info = Map.of(
                "patterns", Map.of(
                        "circuitBreaker", "Prevents cascading failures by opening circuit after threshold",
                        "retry", "Automatically retries failed operations with exponential backoff",
                        "rateLimiter", "Controls the rate of requests per time period",
                        "bulkhead", "Limits concurrent executions to prevent resource exhaustion",
                        "timeLimiter", "Limits execution time and cancels long-running operations"
                ),
                "actuatorEndpoints", Map.of(
                        "health", "/actuator/health",
                        "circuitbreakers", "/actuator/circuitbreakers",
                        "metrics", "/actuator/metrics",
                        "prometheus", "/actuator/prometheus"
                ),
                "zipkin", Map.of(
                        "ui", "http://localhost:9411",
                        "description", "View distributed traces and service dependencies"
                ),
                "configuration", Map.of(
                        "externalService", "Circuit breaker with 50% failure threshold, 5s wait in open state",
                        "database", "Circuit breaker with 20 call window, 5 retry attempts",
                        "api", "Rate limiter allowing 100 requests per second"
                )
        );

        return MyApiResponse.success(info);
    }
}
