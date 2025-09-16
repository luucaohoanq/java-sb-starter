/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.configs;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.config.MeterFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for performance monitoring using Micrometer metrics. This class configures custom
 * metrics and timers for performance testing.
 */
@Configuration
@Slf4j
public class PerformanceMonitoringConfig {

  /** Customizes the meter registry to add common tags and configure metrics. */
  @Bean
  public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
    return registry -> {
      registry
          .config()
          .commonTags("application", "orchidbe")
          .commonTags("version", "1.0.0")
          .meterFilter(
              MeterFilter.deny(
                  id -> {
                    String uri = id.getTag("uri");
                    return uri != null
                        && (uri.startsWith("/actuator") || uri.startsWith("/swagger"));
                  }));

      log.info("📊 Performance monitoring configured with Micrometer");
    };
  }

  /** Custom timer for measuring API response times. */
  @Bean
  public Timer apiResponseTimer(MeterRegistry meterRegistry) {
    return Timer.builder("api.response.time")
        .description("API response time")
        .register(meterRegistry);
  }
}
