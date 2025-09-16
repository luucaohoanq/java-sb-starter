# Performance Testing with Spring Boot, Micrometer, JMeter & k6

This project includes a comprehensive performance testing infrastructure with Spring Boot Actuator, Micrometer metrics, JMeter, and k6 for load testing and performance monitoring.

## 🚀 Features

- **Spring Boot Actuator** - Health checks and application metrics
- **Micrometer & Prometheus** - Custom metrics and monitoring
- **Performance Test Endpoints** - Dedicated endpoints for various performance scenarios
- **JMeter Integration** - GUI and CLI load testing
- **k6 Load Testing** - Modern JavaScript-based load testing
- **Automated Test Scripts** - Shell scripts for running comprehensive performance tests
- **Metrics Collection** - Real-time performance metrics and reporting

## 📁 Project Structure

```
performance-tests/
├── basic-load-test.jmx          # JMeter test plan
├── k6-performance-test.js       # k6 test scenarios
├── run-performance-tests.sh     # Automated test runner
└── performance-results/         # Generated test results
    ├── k6-results-*.json
    ├── k6-results-*.csv
    └── jmeter/
        └── report-*/index.html

src/main/java/com/orchid/orchidbe/
├── configs/
│   └── PerformanceMonitoringConfig.java  # Micrometer configuration
└── domain/performance/
    └── PerformanceTestController.java     # Performance test endpoints
```

## 🎯 Performance Test Endpoints

### Available Endpoints

| Endpoint                                     | Purpose             | Description                      |
| -------------------------------------------- | ------------------- | -------------------------------- |
| `GET /api/performance/fast`                  | Baseline            | Immediate response (~1ms)        |
| `GET /api/performance/slow?delayMs=500`      | Latency Testing     | Configurable delay simulation    |
| `GET /api/performance/variable/{complexity}` | CPU Load            | Variable complexity calculations |
| `GET /api/performance/random`                | Unpredictable Load  | Random response times (50-500ms) |
| `POST /api/performance/data`                 | Data Processing     | JSON payload processing          |
| `GET /api/performance/memory`                | Resource Monitoring | Memory usage statistics          |
| `GET /api/performance/metrics`               | Custom Metrics      | Performance counters and timers  |

### Example Usage

```bash
# Fast baseline test
curl http://localhost:8080/api/performance/fast

# Slow endpoint with 200ms delay
curl http://localhost:8080/api/performance/slow?delayMs=200

# Variable complexity (1-10 scale)
curl http://localhost:8080/api/performance/variable/5

# Memory statistics
curl http://localhost:8080/api/performance/memory

# Custom performance metrics
curl http://localhost:8080/api/performance/metrics
```

## 📊 Monitoring & Metrics

### Actuator Endpoints

| Endpoint               | Purpose                      |
| ---------------------- | ---------------------------- |
| `/actuator/health`     | Application health status    |
| `/actuator/metrics`    | All available metrics        |
| `/actuator/prometheus` | Prometheus-formatted metrics |
| `/actuator/httptrace`  | HTTP request traces          |

### Custom Metrics

The application tracks these custom performance metrics:

- `performance.fast.calls` - Fast endpoint call count
- `performance.slow.calls` - Slow endpoint call count
- `performance.variable.calls` - Variable complexity call count
- `performance.random.calls` - Random endpoint call count
- `performance.memory.calls` - Memory endpoint call count
- `performance.*.timer` - Response time timers for each endpoint

### Accessing Metrics

```bash
# All application metrics
curl http://localhost:8080/actuator/metrics

# HTTP request metrics
curl http://localhost:8080/actuator/metrics/http.server.requests

# JVM memory usage
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Custom performance metrics
curl http://localhost:8080/api/performance/metrics

# Prometheus format (for Grafana integration)
curl http://localhost:8080/actuator/prometheus
```

## 🏃‍♂️ Running Performance Tests

### Prerequisites

1. **Java Application Running:**

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Install Testing Tools (Optional):**

   ```bash
   # Install k6
   sudo apt-get install k6
   # or
   brew install k6

   # Install JMeter
   # Download from: https://jmeter.apache.org/download_jmeter.cgi
   ```

### Quick Start

```bash
# Navigate to performance tests directory
cd performance-tests

# Run all tests (basic + k6 + JMeter)
./run-performance-tests.sh

# Run specific test types
./run-performance-tests.sh basic    # Only curl tests
./run-performance-tests.sh k6       # Only k6 tests
./run-performance-tests.sh jmeter   # Only JMeter tests

# Show help
./run-performance-tests.sh help
```

### Manual Testing

#### Basic curl Testing

```bash
# Test individual endpoints
curl -w "\nResponse Time: %{time_total}s\n" http://localhost:8080/api/performance/fast
curl -w "\nResponse Time: %{time_total}s\n" http://localhost:8080/api/performance/slow?delayMs=200

# Concurrent testing
for i in {1..10}; do
  curl -s -w "%{time_total}\n" -o /dev/null http://localhost:8080/api/performance/fast &
done
wait
```

#### k6 Load Testing

```bash
# Run k6 tests directly
k6 run k6-performance-test.js

# Run with custom output
k6 run --out json=results.json --out csv=results.csv k6-performance-test.js
```

#### JMeter Testing

```bash
# GUI mode (for test development)
jmeter -t basic-load-test.jmx

# CLI mode (for automated testing)
jmeter -n -t basic-load-test.jmx -l results.jtl -e -o report-folder
```

## 📈 Test Scenarios

### k6 Test Scenarios

The k6 script includes multiple test scenarios:

1. **Baseline Test (30s)**

   - 10 concurrent users
   - Fast endpoint only
   - Establishes performance baseline

2. **Load Test (12 minutes)**

   - Ramps from 0 → 20 → 50 users
   - Mixed endpoint testing
   - Realistic usage patterns

3. **Stress Test (10 minutes)**

   - Ramps up to 200 concurrent users
   - Tests system limits
   - Resource-intensive endpoints

4. **Data Processing Test**
   - POST requests with JSON payloads
   - Tests data handling performance

### JMeter Test Plan

The JMeter test plan includes:

- **Thread Groups:** Configurable user load
- **HTTP Request Samplers:** All performance endpoints
- **Listeners:** Response time graphs and reports
- **Assertions:** Response validation
- **Timers:** Realistic user behavior simulation

## 🎯 Performance Thresholds

### k6 Thresholds

```javascript
thresholds: {
  http_req_duration: ['p(95)<500', 'p(99)<1000'], // 95% < 500ms, 99% < 1s
  http_req_failed: ['rate<0.1'],                  // Error rate < 10%
  errors: ['rate<0.1'],                           // Custom error rate < 10%
}
```

### Expected Performance

| Endpoint            | Expected Response Time | Load Capacity |
| ------------------- | ---------------------- | ------------- |
| `/fast`             | < 50ms                 | 1000+ RPS     |
| `/slow?delayMs=200` | ~200ms                 | 100+ RPS      |
| `/variable/5`       | < 100ms                | 500+ RPS      |
| `/random`           | 50-500ms               | 200+ RPS      |
| `/memory`           | < 50ms                 | 500+ RPS      |

## 📋 Test Reports

### Automated Reports

The test runner generates comprehensive reports:

```
performance-results/
├── performance-summary-20250101_120000.md
├── k6-results-20250101_120000.json
├── k6-results-20250101_120000.csv
└── jmeter/
    └── report-20250101_120000/
        ├── index.html          # Main JMeter report
        ├── statistics.json     # Raw statistics
        └── content/            # Detailed charts and graphs
```

### Report Contents

- **Summary Statistics:** Request counts, response times, error rates
- **Performance Trends:** Response time over time
- **Resource Usage:** Memory and CPU consumption
- **Error Analysis:** Failed requests and error patterns
- **Recommendations:** Performance optimization suggestions

## 🔧 Configuration

### Application Configuration

In `application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,httptrace
  metrics:
    web:
      server:
        request:
          autotime:
            enabled: true
            percentiles: 0.5,0.95,0.99
    export:
      prometheus:
        enabled: true
```

### Performance Monitoring

The `PerformanceMonitoringConfig` class configures:

- Common metric tags
- Meter filters (excludes actuator endpoints)
- Custom timers and counters
- Prometheus integration

## 🚨 Best Practices

### Testing Guidelines

1. **Establish Baselines:** Always test fast endpoints first
2. **Gradual Load Increase:** Ramp up users gradually
3. **Monitor Resources:** Watch CPU, memory, and database connections
4. **Test Realistic Scenarios:** Mix different endpoint types
5. **Validate Results:** Check both performance and functionality

### Performance Optimization

1. **Database Optimization:**

   - Use connection pooling
   - Optimize queries
   - Add appropriate indexes

2. **Application Tuning:**

   - Tune JVM parameters
   - Configure thread pools
   - Enable caching where appropriate

3. **Infrastructure:**
   - Load balancing
   - CDN for static content
   - Database read replicas

### Monitoring in Production

1. **Set Up Dashboards:**

   ```bash
   # Prometheus + Grafana integration
   curl http://localhost:8080/actuator/prometheus
   ```

2. **Alert Thresholds:**

   - Response time > 1s
   - Error rate > 5%
   - Memory usage > 80%

3. **Regular Performance Testing:**
   - Automated performance regression tests
   - Load testing before releases
   - Capacity planning

## 🔍 Troubleshooting

### Common Issues

1. **High Response Times:**

   - Check database query performance
   - Monitor thread pool saturation
   - Review garbage collection logs

2. **Memory Issues:**

   - Analyze heap dumps
   - Check for memory leaks
   - Monitor connection pools

3. **Test Failures:**
   - Ensure application is running
   - Check endpoint availability
   - Verify test tool installation

### Debug Commands

```bash
# Check application health
curl http://localhost:8080/actuator/health

# Monitor memory in real-time
curl http://localhost:8080/api/performance/memory

# View all metrics
curl http://localhost:8080/actuator/metrics | jq '.names | sort'

# Test specific endpoint
curl -v http://localhost:8080/api/performance/fast
```

## 📖 Integration Examples

### CI/CD Integration

```yaml
# .github/workflows/performance.yml
name: Performance Tests
on: [push, pull_request]

jobs:
  performance:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Start Application
        run: ./mvnw spring-boot:run &
      - name: Wait for startup
        run: sleep 30
      - name: Run Performance Tests
        run: cd performance-tests && ./run-performance-tests.sh basic
```

### Docker Integration

```dockerfile
# Dockerfile for performance testing
FROM openjdk:17-jdk-slim
COPY target/orchidbe-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
# Run with Docker
docker build -t orchidbe .
docker run -p 8080:8080 orchidbe

# Run performance tests
cd performance-tests && ./run-performance-tests.sh
```

## 🎉 Summary

This performance testing infrastructure provides:

✅ **Comprehensive Metrics** - Custom and standard performance metrics  
✅ **Multiple Testing Tools** - k6, JMeter, and curl-based testing  
✅ **Automated Reporting** - Detailed performance reports and summaries  
✅ **Real-time Monitoring** - Live metrics and health checks  
✅ **Scalable Testing** - From baseline to stress testing scenarios  
✅ **Production Ready** - Prometheus integration for monitoring

The setup enables you to:

- Identify performance bottlenecks
- Validate system capacity
- Monitor production performance
- Ensure performance regression prevention
- Make data-driven optimization decisions

For questions or issues, check the troubleshooting section or review the test logs in `performance-results/`.
