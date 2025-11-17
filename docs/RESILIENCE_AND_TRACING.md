# Distributed Tracing & Resilience Patterns

This project includes distributed tracing with Zipkin and fault tolerance patterns using Resilience4j.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Resilience4j Patterns](#resilience4j-patterns)
- [Distributed Tracing](#distributed-tracing)
- [Monitoring & Observability](#monitoring--observability)
- [Testing](#testing)

## 🎯 Overview

### Distributed Tracing with Micrometer Tracing + Zipkin

- **Micrometer Tracing** (replaces Spring Cloud Sleuth in Spring Boot 3.x)
- **Zipkin** for trace visualization and analysis
- Automatic trace ID propagation across service boundaries
- Integration with existing Prometheus metrics

### Resilience4j Fault Tolerance Patterns

- **Circuit Breaker** - Prevents cascading failures
- **Retry** - Automatic retry with exponential backoff
- **Rate Limiter** - Controls request rate
- **Bulkhead** - Limits concurrent executions
- **Time Limiter** - Limits execution time

## ✨ Features

### 1. Distributed Tracing

- ✅ Automatic trace ID generation and propagation
- ✅ Span creation for HTTP requests
- ✅ Integration with Zipkin UI for visualization
- ✅ Correlation of logs with trace IDs
- ✅ Service dependency analysis

### 2. Fault Tolerance

- ✅ Circuit breaker pattern with configurable thresholds
- ✅ Automatic retry with exponential backoff
- ✅ Rate limiting per API or service
- ✅ Bulkhead isolation for resource protection
- ✅ Time limits on long-running operations
- ✅ Fallback methods for graceful degradation

### 3. Observability

- ✅ Resilience4j metrics exposed via Micrometer
- ✅ Circuit breaker state in health endpoints
- ✅ Event streams for all resilience patterns
- ✅ Prometheus metrics for monitoring
- ✅ Actuator endpoints for runtime inspection

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Docker (for Zipkin)
- Maven 3.8+

### Start with Docker Compose

The easiest way to run the application with Zipkin:

```bash
docker-compose up -d
```

This starts:
- **Application** on port 4040 (mapped from 8080)
- **Zipkin** on port 9411

### Run Locally (without Docker)

1. Start Zipkin separately:
```bash
docker run -d -p 9411:9411 openzipkin/zipkin:latest
```

2. Run the application:
```bash
./mvnw spring-boot:run
```

### Access Points

| Service | URL | Description |
|---------|-----|-------------|
| Application | http://localhost:8080 | Main application |
| Swagger UI | http://localhost:8080/swagger-ui.html | API documentation |
| Zipkin UI | http://localhost:9411 | Distributed tracing |
| Actuator | http://localhost:8080/actuator | Health & metrics |
| Prometheus | http://localhost:8080/actuator/prometheus | Metrics endpoint |

## ⚙️ Configuration

### Tracing Configuration (`application.yml`)

```yaml
management:
  tracing:
    sampling:
      probability: 1.0  # 100% in dev, use 0.1 (10%) in production
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
```

### Resilience4j Configuration

#### Circuit Breaker

```yaml
resilience4j:
  circuitbreaker:
    instances:
      externalService:
        slidingWindowSize: 10              # Number of calls to track
        minimumNumberOfCalls: 5            # Min calls before calculation
        failureRateThreshold: 50           # 50% failure opens circuit
        waitDurationInOpenState: 5s        # Wait before half-open
        permittedNumberOfCallsInHalfOpenState: 3
```

#### Retry

```yaml
resilience4j:
  retry:
    instances:
      externalService:
        maxAttempts: 3                     # Max retry attempts
        waitDuration: 1s                   # Initial wait
        enableExponentialBackoff: true
        exponentialBackoffMultiplier: 2    # 1s, 2s, 4s
```

#### Rate Limiter

```yaml
resilience4j:
  ratelimiter:
    instances:
      api:
        limitForPeriod: 100                # 100 requests
        limitRefreshPeriod: 1s             # per second
        timeoutDuration: 0s                # fail immediately
```

#### Bulkhead

```yaml
resilience4j:
  bulkhead:
    instances:
      externalService:
        maxConcurrentCalls: 10             # Max parallel calls
        maxWaitDuration: 0ms               # fail immediately
```

#### Time Limiter

```yaml
resilience4j:
  timelimiter:
    instances:
      externalService:
        timeoutDuration: 3s                # Timeout after 3s
        cancelRunningFuture: true
```

## 🛡️ Resilience4j Patterns

### 1. Circuit Breaker

**Purpose:** Prevent cascading failures by stopping calls to failing services.

**States:**
- **CLOSED** - Normal operation, all calls go through
- **OPEN** - Circuit is open, calls fail immediately (fast-fail)
- **HALF_OPEN** - Testing if service recovered

**Usage Example:**

```java
@CircuitBreaker(name = "externalService", fallbackMethod = "fallback")
public String callExternalService() {
    return restTemplate.getForObject("http://external-service/api", String.class);
}

private String fallback(Exception ex) {
    return "Fallback response";
}
```

**Test it:**
```bash
curl "http://localhost:8080/api/resilience/circuit-breaker?shouldFail=true"
```

### 2. Retry

**Purpose:** Automatically retry failed operations with backoff strategy.

**Configuration:**
- Max attempts: 3
- Wait duration: 1s, 2s, 4s (exponential backoff)
- Retry on: HttpServerErrorException, TimeoutException, IOException

**Usage Example:**

```java
@Retry(name = "externalService", fallbackMethod = "retryFallback")
public String callWithRetry() {
    return externalService.getData();
}
```

**Test it:**
```bash
curl "http://localhost:8080/api/resilience/retry?shouldFail=true"
```

### 3. Rate Limiter

**Purpose:** Control the rate of requests to prevent overload.

**Configuration:**
- API rate limiter: 100 requests/second
- External service: 50 requests/second

**Usage Example:**

```java
@RateLimiter(name = "api", fallbackMethod = "rateLimiterFallback")
public String callWithRateLimit() {
    return processRequest();
}
```

**Test it:**
```bash
for i in {1..150}; do curl "http://localhost:8080/api/resilience/rate-limiter" & done
```

### 4. Bulkhead

**Purpose:** Limit concurrent executions to prevent resource exhaustion.

**Configuration:**
- External service: Max 10 concurrent calls
- Database: Max 20 concurrent calls

**Usage Example:**

```java
@Bulkhead(name = "externalService", fallbackMethod = "bulkheadFallback")
public String callWithBulkhead() {
    return longRunningOperation();
}
```

**Test it:**
```bash
for i in {1..20}; do curl "http://localhost:8080/api/resilience/bulkhead" & done
```

### 5. Time Limiter

**Purpose:** Limit execution time and cancel long-running operations.

**Configuration:**
- External service timeout: 3 seconds

**Usage Example:**

```java
@TimeLimiter(name = "externalService", fallbackMethod = "timeLimiterFallback")
public CompletableFuture<String> callWithTimeLimit() {
    return CompletableFuture.supplyAsync(() -> longOperation());
}
```

**Test it:**
```bash
curl "http://localhost:8080/api/resilience/time-limiter?delayMs=5000"
```

### 6. Combined Patterns

Multiple patterns can work together:

```java
@CircuitBreaker(name = "externalService")
@Retry(name = "externalService")
@RateLimiter(name = "api")
public String robustCall() {
    return externalService.call();
}
```

## 🔍 Distributed Tracing

### How It Works

1. **Trace ID Generation**: Automatically generated for each incoming request
2. **Span Creation**: Each operation creates a span with timing information
3. **Context Propagation**: Trace ID propagates across service calls
4. **Data Export**: Spans exported to Zipkin for visualization

### View Traces in Zipkin

1. Open Zipkin UI: http://localhost:9411
2. Click "Run Query" to see recent traces
3. Click on a trace to see detailed span information
4. View service dependencies in the dependency graph

### Trace Information Includes

- **Trace ID**: Unique identifier for the entire request flow
- **Span ID**: Unique identifier for each operation
- **Duration**: Time taken for each operation
- **Tags**: Metadata (HTTP method, status code, etc.)
- **Logs**: Events within a span

### Correlation with Logs

Logs automatically include trace IDs for correlation:

```
2025-11-16 10:30:45.123 [trace-id-here] INFO  c.o.o.controller.ResilienceController - Request received
```

## 📊 Monitoring & Observability

### Actuator Endpoints

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Overall health including circuit breakers |
| `/actuator/circuitbreakers` | All circuit breaker states |
| `/actuator/circuitbreakerevents` | Recent circuit breaker events |
| `/actuator/retries` | All retry instances |
| `/actuator/ratelimiters` | All rate limiter states |
| `/actuator/bulkheads` | All bulkhead states |
| `/actuator/metrics` | All Micrometer metrics |
| `/actuator/prometheus` | Prometheus format metrics |

### Health Indicators

Circuit breakers and rate limiters automatically contribute to health:

```bash
curl http://localhost:8080/actuator/health
```

Response includes:
```json
{
  "status": "UP",
  "components": {
    "circuitBreakers": {
      "status": "UP",
      "details": {
        "externalService": {
          "state": "CLOSED",
          "failureRate": "0.0%",
          "slowCallRate": "0.0%"
        }
      }
    }
  }
}
```

### Metrics

All Resilience4j patterns emit metrics:

- `resilience4j.circuitbreaker.calls` - Circuit breaker call counts
- `resilience4j.retry.calls` - Retry attempt counts
- `resilience4j.ratelimiter.calls` - Rate limiter call counts
- `resilience4j.bulkhead.calls` - Bulkhead call counts

View metrics:
```bash
curl http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.calls
```

### Event Streams

Monitor real-time events:

```bash
# Circuit breaker events
curl http://localhost:8080/actuator/circuitbreakerevents

# All events for a specific circuit breaker
curl http://localhost:8080/actuator/circuitbreakerevents/externalService
```

## 🧪 Testing

### Test Endpoints

All resilience patterns can be tested via REST API:

```bash
# Test Circuit Breaker
curl "http://localhost:8080/api/resilience/circuit-breaker?shouldFail=false"

# Test Retry
curl "http://localhost:8080/api/resilience/retry?shouldFail=true"

# Test Rate Limiter (rapid calls)
for i in {1..150}; do curl "http://localhost:8080/api/resilience/rate-limiter" & done

# Test Bulkhead (concurrent calls)
for i in {1..20}; do curl "http://localhost:8080/api/resilience/bulkhead" & done

# Test Time Limiter
curl "http://localhost:8080/api/resilience/time-limiter?delayMs=5000"

# Test Combined Patterns
curl "http://localhost:8080/api/resilience/combined?shouldFail=false"

# Test Database Resilience
curl "http://localhost:8080/api/resilience/database?shouldFail=true"

# Get Information
curl "http://localhost:8080/api/resilience/info"
```

### Monitoring Tests

1. **Check Circuit Breaker State:**
```bash
curl http://localhost:8080/actuator/circuitbreakers
```

2. **View Recent Events:**
```bash
curl http://localhost:8080/actuator/circuitbreakerevents
```

3. **Check Health:**
```bash
curl http://localhost:8080/actuator/health
```

4. **View Metrics:**
```bash
curl http://localhost:8080/actuator/metrics | jq '.names | map(select(contains("resilience4j")))'
```

### Load Testing

Use tools like Apache Bench or k6 for load testing:

```bash
# Apache Bench
ab -n 1000 -c 10 http://localhost:8080/api/resilience/rate-limiter

# k6 (if installed)
k6 run performance-tests/k6-performance-test.js
```

## 📚 Additional Resources

### Resilience4j Documentation

- [Official Documentation](https://resilience4j.readme.io/)
- [Circuit Breaker](https://resilience4j.readme.io/docs/circuitbreaker)
- [Retry](https://resilience4j.readme.io/docs/retry)
- [Rate Limiter](https://resilience4j.readme.io/docs/ratelimiter)
- [Bulkhead](https://resilience4j.readme.io/docs/bulkhead)
- [Time Limiter](https://resilience4j.readme.io/docs/timelimiter)

### Micrometer Tracing

- [Micrometer Tracing Documentation](https://micrometer.io/docs/tracing)
- [Spring Boot Observability](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3)

### Zipkin

- [Zipkin Documentation](https://zipkin.io/)
- [OpenZipkin GitHub](https://github.com/openzipkin/zipkin)

## 🔧 Troubleshooting

### Zipkin Not Receiving Traces

1. Check Zipkin is running:
```bash
docker ps | grep zipkin
```

2. Check application logs for Zipkin connection errors

3. Verify ZIPKIN_URL environment variable:
```bash
echo $ZIPKIN_URL
```

### Circuit Breaker Not Opening

1. Check minimum number of calls threshold
2. Verify failure rate threshold
3. View circuit breaker events:
```bash
curl http://localhost:8080/actuator/circuitbreakerevents/externalService
```

### Rate Limiter Not Working

1. Check configuration in application.yml
2. Verify you're exceeding the limit
3. Check rate limiter metrics:
```bash
curl http://localhost:8080/actuator/metrics/resilience4j.ratelimiter.available.permissions
```

## 🎓 Best Practices

1. **Production Sampling**: Reduce trace sampling to 10-20% in production
2. **Fallback Methods**: Always provide meaningful fallback responses
3. **Monitoring**: Set up alerts on circuit breaker state changes
4. **Testing**: Test resilience patterns under load before production
5. **Configuration**: Tune thresholds based on your SLAs
6. **Logging**: Use structured logging with trace IDs
7. **Documentation**: Document expected behavior of fallback methods

## 📝 License

Copyright (c) 2025 lcaohoanq. All rights reserved.
