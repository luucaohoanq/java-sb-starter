# Architecture Diagram - Resilience4j & Distributed Tracing

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           Spring Boot Application                            │
│                                (Port 8080)                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌────────────────────────────────────────────────────────────────────┐    │
│  │                      ResilienceController                           │    │
│  │                    /api/resilience/*                                │    │
│  │  • circuit-breaker   • retry         • rate-limiter                │    │
│  │  • bulkhead         • time-limiter   • combined                    │    │
│  └────────────────────┬───────────────────────────────────────────────┘    │
│                       │                                                      │
│  ┌────────────────────▼───────────────────────────────────────────────┐    │
│  │                      ResilientService                               │    │
│  │  ┌──────────────────────────────────────────────────────────────┐ │    │
│  │  │ @CircuitBreaker  @Retry  @RateLimiter                         │ │    │
│  │  │ @Bulkhead  @TimeLimiter                                       │ │    │
│  │  └──────────────────────────────────────────────────────────────┘ │    │
│  └────────────────────┬───────────────────────────────────────────────┘    │
│                       │                                                      │
│  ┌────────────────────▼───────────────────────────────────────────────┐    │
│  │              Resilience4j Configuration                            │    │
│  │  • CircuitBreakerRegistry    • RetryRegistry                       │    │
│  │  • RateLimiterRegistry        • BulkheadRegistry                   │    │
│  │  • TimeLimiterRegistry        • Event Consumers                    │    │
│  └────────────────────┬───────────────────────────────────────────────┘    │
│                       │                                                      │
│  ┌────────────────────▼───────────────────────────────────────────────┐    │
│  │                  Micrometer Tracing                                 │    │
│  │  • Trace ID Generation       • Span Creation                       │    │
│  │  • Context Propagation       • Brave Bridge                        │    │
│  └────────────────────┬───────────────────────────────────────────────┘    │
│                       │                                                      │
│  ┌────────────────────▼───────────────────────────────────────────────┐    │
│  │                    Micrometer Registry                              │    │
│  │  • Resilience4j Metrics      • Application Metrics                 │    │
│  │  • HTTP Request Metrics      • JVM Metrics                         │    │
│  └────────────────────┬───────────────────────────────────────────────┘    │
│                       │                                                      │
│  ┌────────────────────▼───────────────────────────────────────────────┐    │
│  │               Spring Boot Actuator                                  │    │
│  │  /actuator/health          /actuator/circuitbreakers               │    │
│  │  /actuator/metrics         /actuator/prometheus                    │    │
│  │  /actuator/circuitbreakerevents                                    │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                               │
└───────────────────┬──────────────────────┬────────────────────────────────┘
                    │                      │
         ┌──────────▼─────────┐  ┌────────▼────────┐
         │   Zipkin Server    │  │  Prometheus     │
         │   (Port 9411)      │  │  (Optional)     │
         │                    │  │                 │
         │  • Trace Storage   │  │  • Metrics      │
         │  • UI Dashboard    │  │  • Alerting     │
         │  • Dependencies    │  │  • Graphing     │
         └────────────────────┘  └─────────────────┘
```

## Request Flow with Tracing

```
1. HTTP Request
   │
   ├─→ [Micrometer Tracing]
   │   • Generate Trace ID (if not present)
   │   • Create Span
   │   • Add to MDC for logging
   │
   ├─→ [Controller Layer]
   │   • ResilienceController receives request
   │   • Span: "http-request"
   │
   ├─→ [Resilience4j Patterns Applied]
   │   │
   │   ├─→ [Rate Limiter]
   │   │   • Check if within limit
   │   │   • Reject if exceeded
   │   │
   │   ├─→ [Circuit Breaker]
   │   │   • Check state (CLOSED/OPEN/HALF_OPEN)
   │   │   • Fast-fail if OPEN
   │   │
   │   ├─→ [Bulkhead]
   │   │   • Check concurrent calls
   │   │   • Reject if at capacity
   │   │
   │   └─→ [Service Execution]
   │       • Span: "service-call"
   │       │
   │       ├─→ [Retry] (if fails)
   │       │   • Wait with backoff
   │       │   • Attempt again
   │       │
   │       └─→ [Time Limiter]
   │           • Monitor execution time
   │           • Cancel if timeout
   │
   ├─→ [Response or Fallback]
   │   • Return success or fallback
   │   • Record metrics
   │
   └─→ [Trace Export]
       • Complete spans
       • Export to Zipkin
       • Update metrics
```

## Circuit Breaker State Machine

```
                    ┌─────────────┐
                    │   CLOSED    │
                    │  (Normal)   │
                    └──────┬──────┘
                           │
                Failure Rate > 50%
                Min Calls = 5
                           │
                           ▼
                    ┌─────────────┐
            ┌───────┤    OPEN     │
            │       │ (Fast-Fail) │
            │       └──────┬──────┘
            │              │
            │      Wait 5 seconds
            │              │
            │              ▼
            │       ┌─────────────┐
            │       │ HALF_OPEN   │
            │       │  (Testing)  │
            │       └──────┬──────┘
            │              │
    Success Rate < 50%     │      Success Rate ≥ 50%
            │              │              │
            └──────────────┴──────────────┘
```

## Resilience4j Patterns Interaction

```
Request Flow Through Patterns:

HTTP Request
    │
    ├─→ [1. Rate Limiter] ──X─→ 429 Too Many Requests
    │       ↓ (within limit)
    │
    ├─→ [2. Circuit Breaker] ──X─→ 503 Service Unavailable (Fast-Fail)
    │       ↓ (circuit closed)
    │
    ├─→ [3. Bulkhead] ──X─→ 503 Capacity Exceeded
    │       ↓ (capacity available)
    │
    ├─→ [4. Time Limiter]
    │       ↓
    │   ┌───▼────┐
    │   │ RETRY  │ ←─┐ (if fails)
    │   │ LOGIC  │ ──┤
    │   └───┬────┘   │
    │       │        │ Max 3 attempts
    │       ↓        │ with backoff
    │   [Service]────┘
    │       │
    │       ├─→ Success ────→ 200 OK
    │       │
    │       └─→ All Retries Failed
    │              │
    │              ├─→ Update Circuit Breaker Failure Count
    │              │
    │              └─→ Fallback Method ──→ 200 OK (Degraded)
```

## Metrics & Observability Flow

```
┌──────────────────────────────────────────────────────────────┐
│                  Application Execution                        │
└──────────────────┬───────────────────────────────────────────┘
                   │
        ┌──────────┼──────────┐
        │          │          │
        ▼          ▼          ▼
┌───────────┐ ┌────────┐ ┌──────────────┐
│Resilience4j│ │Tracing │ │ Application │
│  Events   │ │ Spans  │ │   Metrics   │
└─────┬─────┘ └────┬───┘ └──────┬───────┘
      │            │            │
      └────────────┼────────────┘
                   │
                   ▼
           ┌───────────────┐
           │  Micrometer   │
           │   Registry    │
           └───────┬───────┘
                   │
        ┌──────────┼──────────┐
        │          │          │
        ▼          ▼          ▼
┌─────────────┐ ┌────────┐ ┌──────────┐
│  Actuator   │ │ Zipkin │ │Prometheus│
│  Endpoints  │ │  UI    │ │  Export  │
└─────────────┘ └────────┘ └──────────┘
        │          │            │
        ▼          ▼            ▼
┌─────────────────────────────────────┐
│     Monitoring & Alerting           │
│  • Grafana Dashboards               │
│  • Alert Manager                    │
│  • Log Aggregation                  │
└─────────────────────────────────────┘
```

## Docker Compose Architecture

```
┌─────────────────────────────────────────────────────────┐
│              Docker Compose Network (sba)               │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  ┌────────────────────┐        ┌──────────────────┐   │
│  │  orchidbe         │         │    zipkin        │   │
│  │  (Spring Boot)    │────────▶│  (OpenZipkin)    │   │
│  │                   │ traces  │                  │   │
│  │  Port: 4040→8080 │         │  Port: 9411      │   │
│  │                   │         │                  │   │
│  │  Environment:     │         │  Storage: Memory │   │
│  │  ZIPKIN_URL       │         │                  │   │
│  └────────────────────┘         └──────────────────┘   │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

## Endpoint Hierarchy

```
http://localhost:8080
│
├─ /api/resilience/
│  ├─ /circuit-breaker     [Circuit Breaker Demo]
│  ├─ /retry               [Retry Demo]
│  ├─ /rate-limiter        [Rate Limiter Demo]
│  ├─ /bulkhead            [Bulkhead Demo]
│  ├─ /time-limiter        [Time Limiter Demo]
│  ├─ /combined            [Multiple Patterns]
│  ├─ /database            [DB Resilience]
│  └─ /info                [Configuration Info]
│
├─ /actuator/
│  ├─ /health              [Health Status + Circuit Breakers]
│  ├─ /metrics             [All Metrics]
│  ├─ /prometheus          [Prometheus Format]
│  ├─ /circuitbreakers     [Circuit Breaker States]
│  ├─ /circuitbreakerevents[CB Events]
│  ├─ /retries             [Retry Instances]
│  ├─ /ratelimiters        [Rate Limiter States]
│  └─ /bulkheads           [Bulkhead States]
│
└─ /swagger-ui.html        [API Documentation]

http://localhost:9411       [Zipkin UI]
```

## Pattern Decision Tree

```
When to Use Which Pattern?

Service Call Scenario
    │
    ├─ High failure rate?
    │  └─→ Use Circuit Breaker
    │      • Fast-fail when service is down
    │      • Automatic recovery testing
    │
    ├─ Transient failures?
    │  └─→ Use Retry
    │      • Network glitches
    │      • Temporary unavailability
    │
    ├─ Need to control load?
    │  └─→ Use Rate Limiter
    │      • Protect backend services
    │      • Fair usage policies
    │
    ├─ Limited resources?
    │  └─→ Use Bulkhead
    │      • Prevent resource exhaustion
    │      • Isolate failure domains
    │
    ├─ Long-running operations?
    │  └─→ Use Time Limiter
    │      • Prevent thread starvation
    │      • Enforce SLAs
    │
    └─ Critical path?
       └─→ Use Combined Patterns
           • Circuit Breaker + Retry
           • All patterns together
```

---

**Legend:**
- `─→` : Synchronous flow
- `──X─→` : Rejection/Failure path
- `←─┐` : Retry loop
- `▼` : Continues to next step
- `├─→` : Branch/Fork
