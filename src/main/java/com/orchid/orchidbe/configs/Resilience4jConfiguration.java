/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.configs;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Resilience4j patterns.
 * This class configures Circuit Breaker, Retry, and other fault tolerance patterns.
 */
@Configuration
@Slf4j
public class Resilience4jConfiguration {

    /**
     * Configure event consumer for CircuitBreaker registry.
     * Logs when circuit breakers are added, removed, or replaced.
     */
    @Bean
    public RegistryEventConsumer<CircuitBreaker> circuitBreakerEventConsumer() {
        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                CircuitBreaker circuitBreaker = entryAddedEvent.getAddedEntry();
                log.info("CircuitBreaker {} added", circuitBreaker.getName());

                circuitBreaker.getEventPublisher()
                        .onEvent(event -> log.info("CircuitBreaker event: {}", event));

                circuitBreaker.getEventPublisher()
                        .onStateTransition(event ->
                                log.warn("CircuitBreaker {} state transition: from {} to {}",
                                        circuitBreaker.getName(),
                                        event.getStateTransition().getFromState(),
                                        event.getStateTransition().getToState()));

                circuitBreaker.getEventPublisher()
                        .onError(event ->
                                log.error("CircuitBreaker {} error: {}",
                                        circuitBreaker.getName(),
                                        event.getThrowable().getMessage()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                log.info("CircuitBreaker {} removed", entryRemoveEvent.getRemovedEntry().getName());
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                log.info("CircuitBreaker {} replaced",
                        entryReplacedEvent.getNewEntry().getName());
            }
        };
    }

    /**
     * Configure event consumer for Retry registry.
     * Logs when retry instances are added, removed, or replaced.
     */
    @Bean
    public RegistryEventConsumer<Retry> retryEventConsumer() {
        return new RegistryEventConsumer<Retry>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                Retry retry = entryAddedEvent.getAddedEntry();
                log.info("Retry {} added", retry.getName());

                retry.getEventPublisher()
                        .onEvent(event -> log.debug("Retry event: {}", event));

                retry.getEventPublisher()
                        .onRetry(event ->
                                log.warn("Retry {} attempt {} for operation",
                                        retry.getName(),
                                        event.getNumberOfRetryAttempts()));

                retry.getEventPublisher()
                        .onError(event ->
                                log.error("Retry {} exhausted after {} attempts",
                                        retry.getName(),
                                        event.getNumberOfRetryAttempts()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
                log.info("Retry {} removed", entryRemoveEvent.getRemovedEntry().getName());
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
                log.info("Retry {} replaced", entryReplacedEvent.getNewEntry().getName());
            }
        };
    }

    /**
     * Register custom circuit breakers programmatically if needed.
     * This is an example of how to create circuit breakers at runtime.
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(
            RegistryEventConsumer<CircuitBreaker> circuitBreakerEventConsumer) {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
        registry.getEventPublisher().onEntryAdded(circuitBreakerEventConsumer::onEntryAddedEvent);
        registry.getEventPublisher().onEntryRemoved(circuitBreakerEventConsumer::onEntryRemovedEvent);
        registry.getEventPublisher().onEntryReplaced(circuitBreakerEventConsumer::onEntryReplacedEvent);
        return registry;
    }

    /**
     * Register retry instances with event consumers.
     */
    @Bean
    public RetryRegistry retryRegistry(RegistryEventConsumer<Retry> retryEventConsumer) {
        RetryRegistry registry = RetryRegistry.ofDefaults();
        registry.getEventPublisher().onEntryAdded(retryEventConsumer::onEntryAddedEvent);
        registry.getEventPublisher().onEntryRemoved(retryEventConsumer::onEntryRemovedEvent);
        registry.getEventPublisher().onEntryReplaced(retryEventConsumer::onEntryReplacedEvent);
        return registry;
    }
}
