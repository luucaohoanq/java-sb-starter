import http from "k6/http";
import { check, sleep } from "k6";
import { Rate } from "k6/metrics";

// Custom metrics
export const errorRate = new Rate("errors");

// Test configuration
export const options = {
  scenarios: {
    // Baseline test - fast endpoint
    baseline: {
      executor: "constant-vus",
      vus: 10,
      duration: "30s",
      exec: "testFastEndpoint",
      tags: { test_type: "baseline" },
    },

    // Load test - multiple endpoints
    load_test: {
      executor: "ramping-vus",
      startVUs: 0,
      stages: [
        { duration: "2m", target: 20 }, // Ramp up
        { duration: "5m", target: 20 }, // Stay at 20 users
        { duration: "2m", target: 50 }, // Ramp up to 50 users
        { duration: "5m", target: 50 }, // Stay at 50 users
        { duration: "2m", target: 0 }, // Ramp down
      ],
      exec: "loadTest",
      tags: { test_type: "load" },
    },

    // Stress test - push the limits
    stress_test: {
      executor: "ramping-vus",
      startVUs: 0,
      stages: [
        { duration: "1m", target: 100 }, // Ramp up to 100 users
        { duration: "3m", target: 100 }, // Stay at 100 users
        { duration: "1m", target: 200 }, // Ramp up to 200 users
        { duration: "3m", target: 200 }, // Stay at 200 users
        { duration: "2m", target: 0 }, // Ramp down
      ],
      exec: "stressTest",
      tags: { test_type: "stress" },
    },
  },

  thresholds: {
    http_req_duration: ["p(95)<500", "p(99)<1000"], // 95% of requests under 500ms, 99% under 1s
    http_req_failed: ["rate<0.1"], // Error rate under 10%
    errors: ["rate<0.1"],
  },
};

const BASE_URL = "http://localhost:8080/api";

// Test the fast endpoint (baseline)
export function testFastEndpoint() {
  const response = http.get(`${BASE_URL}/performance/fast`);

  const result = check(response, {
    "fast endpoint status is 200": (r) => r.status === 200,
    "fast endpoint response time < 100ms": (r) => r.timings.duration < 100,
    "fast endpoint returns message": (r) =>
      r.json("message") === "Fast response",
  });

  errorRate.add(!result);
  sleep(0.1); // Short pause between requests
}

// Load test - mix of different endpoints
export function loadTest() {
  const endpoints = [
    { url: "/performance/fast", weight: 40 },
    { url: "/performance/slow?delayMs=200", weight: 30 },
    { url: "/performance/variable/5", weight: 20 },
    { url: "/performance/random", weight: 10 },
  ];

  // Randomly select endpoint based on weight
  const rand = Math.random() * 100;
  let cumWeight = 0;
  let selectedEndpoint = endpoints[0].url;

  for (const endpoint of endpoints) {
    cumWeight += endpoint.weight;
    if (rand <= cumWeight) {
      selectedEndpoint = endpoint.url;
      break;
    }
  }

  const response = http.get(`${BASE_URL}${selectedEndpoint}`);

  const result = check(response, {
    "status is 200": (r) => r.status === 200,
    "response time < 2000ms": (r) => r.timings.duration < 2000,
  });

  errorRate.add(!result);
  sleep(Math.random() * 2); // Random pause 0-2 seconds
}

// Stress test - focus on resource intensive endpoints
export function stressTest() {
  const endpoints = [
    "/performance/variable/10",
    "/performance/slow?delayMs=500",
    "/performance/memory",
  ];

  const endpoint = endpoints[Math.floor(Math.random() * endpoints.length)];
  const response = http.get(`${BASE_URL}${endpoint}`);

  const result = check(response, {
    "stress test status is 200": (r) => r.status === 200,
    "stress test response time < 5000ms": (r) => r.timings.duration < 5000,
  });

  errorRate.add(!result);
  sleep(0.5); // Short pause
}

// Data processing test with POST requests
export function testDataProcessing() {
  const testData = {
    name: "Performance Test",
    timestamp: Date.now(),
    data: Array.from({ length: 100 }, (_, i) => ({
      id: i,
      value: Math.random(),
    })),
  };

  const params = {
    headers: { "Content-Type": "application/json" },
  };

  const response = http.post(
    `${BASE_URL}/performance/data`,
    JSON.stringify(testData),
    params
  );

  const result = check(response, {
    "data processing status is 200": (r) => r.status === 200,
    "data processing response time < 3000ms": (r) => r.timings.duration < 3000,
    "response contains processed message": (r) =>
      r.json("message") === "Data processed successfully",
  });

  errorRate.add(!result);
}

// Setup function - runs once before all tests
export function setup() {
  console.log("🚀 Starting performance tests...");

  // Test if the application is running
  const response = http.get(`${BASE_URL}/performance/fast`);
  if (response.status !== 200) {
    throw new Error("Application is not running or not accessible");
  }

  console.log("✅ Application is responsive, starting tests");
  return { startTime: Date.now() };
}

// Teardown function - runs once after all tests
export function teardown(data) {
  const duration = (Date.now() - data.startTime) / 1000;
  console.log(`🏁 Performance tests completed in ${duration} seconds`);
}
