/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe;

import io.github.lcaohoanq.annotations.BrowserLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.orchid.orchidbe.repositories")
@EntityScan(basePackages = "com.orchid.orchidbe.domain")
@BrowserLauncher(
    value = "http://localhost:8080/swagger-ui.html",
    healthCheckEndpoint = "http://localhost:8080/actuator/health",
    excludeProfiles = {"h2", "test"})
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
