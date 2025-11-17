/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.resilience;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service demonstrating Resilience4j patterns:
 * - Circuit Breaker: Prevents cascading failures
 * - Retry: Automatically retries failed operations
 * - Rate Limiter: Controls the rate of requests
 * - Bulkhead: Limits concurrent executions
 * - Time Limiter: Limits execution time
 */
@Service
@Slf4j
public class ResilientService {

    private final Random random = new Random();

    /**
     * Circuit Breaker example: Protects against cascading failures
     * Opens circuit after 50% failure rate in sliding window of 10 calls
     */
    @CircuitBreaker(name = "externalService", fallbackMethod = "externalServiceFallback")
    public String callExternalService(boolean shouldFail) {
        log.info("Calling external service at {}", LocalDateTime.now());

        if (shouldFail || random.nextInt(10) > 6) {
            log.error("External service call failed");
            throw new RuntimeException("External service is unavailable");
        }

        return "Success from external service at " + LocalDateTime.now();
    }

    private String externalServiceFallback(boolean shouldFail, Exception ex) {
        log.warn("Circuit breaker fallback triggered: {}", ex.getMessage());
        return "Fallback response: Service temporarily unavailable. Please try again later.";
    }

    /**
     * Retry example: Retries failed operations with exponential backoff
     * Will retry up to 3 times with 1s, 2s, 4s wait duration
     */
    @Retry(name = "externalService", fallbackMethod = "retryFallback")
    public String callWithRetry(boolean shouldFail) {
        log.info("Attempting call with retry at {}", LocalDateTime.now());

        if (shouldFail) {
            log.warn("Call failed, will retry...");
            throw new RuntimeException("Service temporarily unavailable");
        }

        return "Success with retry at " + LocalDateTime.now();
    }

    private String retryFallback(boolean shouldFail, Exception ex) {
        log.error("All retry attempts exhausted: {}", ex.getMessage());
        return "Fallback response: Maximum retry attempts reached. Please try again later.";
    }

    /**
     * Rate Limiter example: Limits the number of calls per time period
     * Allows 50 calls per second by default
     */
    @RateLimiter(name = "api", fallbackMethod = "rateLimiterFallback")
    public String callWithRateLimit() {
        log.debug("Rate limited call at {}", LocalDateTime.now());
        return "Success with rate limiting at " + LocalDateTime.now();
    }

    private String rateLimiterFallback(Exception ex) {
        log.warn("Rate limit exceeded: {}", ex.getMessage());
        return "Fallback response: Too many requests. Please slow down.";
    }

    /**
     * Bulkhead example: Limits concurrent executions
     * Allows maximum 10 concurrent calls by default
     */
    @Bulkhead(name = "externalService", fallbackMethod = "bulkheadFallback")
    public String callWithBulkhead() {
        log.info("Bulkhead protected call at {}", LocalDateTime.now());

        try {
            // Simulate some processing
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted", e);
        }

        return "Success with bulkhead at " + LocalDateTime.now();
    }

    private String bulkheadFallback(Exception ex) {
        log.warn("Bulkhead limit reached: {}", ex.getMessage());
        return "Fallback response: System is at capacity. Please try again later.";
    }

    /**
     * Time Limiter example: Limits execution time
     * Times out after 2 seconds by default
     * Note: TimeLimiter requires CompletableFuture
     */
    @TimeLimiter(name = "externalService", fallbackMethod = "timeLimiterFallback")
    public CompletableFuture<String> callWithTimeLimit(long delayMs) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Time limited call started at {}", LocalDateTime.now());

            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted", e);
            }

            return "Success with time limit at " + LocalDateTime.now();
        });
    }

    private CompletableFuture<String> timeLimiterFallback(long delayMs, Exception ex) {
        log.warn("Time limit exceeded: {}", ex.getMessage());
        return CompletableFuture.completedFuture(
                "Fallback response: Operation took too long. Please try again.");
    }

    /**
     * Combined example: Uses Circuit Breaker + Retry + Rate Limiter
     * Shows how multiple patterns can work together
     */
    @CircuitBreaker(name = "externalService", fallbackMethod = "combinedFallback")
    @Retry(name = "externalService")
    @RateLimiter(name = "api")
    public String callWithCombinedPatterns(boolean shouldFail) {
        log.info("Combined patterns call at {}", LocalDateTime.now());

        if (shouldFail || random.nextInt(10) > 7) {
            throw new RuntimeException("Combined call failed");
        }

        return "Success with combined patterns at " + LocalDateTime.now();
    }

    private String combinedFallback(boolean shouldFail, Exception ex) {
        log.error("Combined patterns fallback triggered: {}", ex.getMessage());
        return "Fallback response: Service unavailable. All resilience patterns triggered.";
    }

    /**
     * Database call example with Circuit Breaker and Retry
     */
    @CircuitBreaker(name = "database", fallbackMethod = "databaseFallback")
    @Retry(name = "database")
    public String callDatabase(boolean shouldFail) {
        log.info("Database call at {}", LocalDateTime.now());

        if (shouldFail) {
            throw new RuntimeException("Database connection failed");
        }

        return "Database query successful at " + LocalDateTime.now();
    }

    private String databaseFallback(boolean shouldFail, Exception ex) {
        log.error("Database fallback triggered: {}", ex.getMessage());
        return "Fallback response: Database temporarily unavailable. Using cached data.";
    }
}
