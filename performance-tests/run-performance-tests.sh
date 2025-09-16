#!/bin/bash

# Performance Testing Suite for Orchid API
# This script runs various performance tests and generates reports

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
API_URL="http://localhost:8080"
RESULTS_DIR="./performance-results"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

echo -e "${BLUE}🚀 Orchid API Performance Testing Suite${NC}"
echo "=================================================="

# Create results directory
mkdir -p "$RESULTS_DIR"

# Function to check if service is running
check_service() {
    echo -e "${YELLOW}🔍 Checking if Orchid API is running...${NC}"
    
    if curl -s -f "$API_URL/actuator/health" > /dev/null; then
        echo -e "${GREEN}✅ API is running and healthy${NC}"
        return 0
    else
        echo -e "${RED}❌ API is not accessible at $API_URL${NC}"
        echo "Please start the application first: ./mvnw spring-boot:run"
        exit 1
    fi
}

# Function to run basic curl tests
run_basic_tests() {
    echo -e "${YELLOW}🧪 Running basic endpoint tests...${NC}"
    
    echo "Testing fast endpoint..."
    RESPONSE=$(curl -s "$API_URL/api/performance/fast")
    echo "$RESPONSE" | jq .
    RESPONSE_TIME=$(curl -s -w "%{time_total}" -o /dev/null "$API_URL/api/performance/fast")
    echo "Response Time: ${RESPONSE_TIME}s"
    
    echo -e "\nTesting slow endpoint..."
    RESPONSE=$(curl -s "$API_URL/api/performance/slow?delayMs=200")
    echo "$RESPONSE" | jq .
    RESPONSE_TIME=$(curl -s -w "%{time_total}" -o /dev/null "$API_URL/api/performance/slow?delayMs=200")
    echo "Response Time: ${RESPONSE_TIME}s"
    
    echo -e "\nTesting variable complexity..."
    RESPONSE=$(curl -s "$API_URL/api/performance/variable/5")
    echo "$RESPONSE" | jq .
    RESPONSE_TIME=$(curl -s -w "%{time_total}" -o /dev/null "$API_URL/api/performance/variable/5")
    echo "Response Time: ${RESPONSE_TIME}s"
    
    echo -e "\nTesting memory stats..."
    RESPONSE=$(curl -s "$API_URL/api/performance/memory")
    echo "$RESPONSE" | jq .
    RESPONSE_TIME=$(curl -s -w "%{time_total}" -o /dev/null "$API_URL/api/performance/memory")
    echo "Response Time: ${RESPONSE_TIME}s"
}

# Function to run K6 tests
run_k6_tests() {
    echo -e "${YELLOW}🏃 Running K6 performance tests...${NC}"
    
    if command -v k6 &> /dev/null; then
        echo "Running K6 load tests..."
        k6 run --out json="$RESULTS_DIR/k6-results-$TIMESTAMP.json" \
               --out csv="$RESULTS_DIR/k6-results-$TIMESTAMP.csv" \
               ./k6-performance-test.js
        
        echo -e "${GREEN}✅ K6 tests completed${NC}"
    else
        echo -e "${YELLOW}⚠️ K6 not installed. Skipping K6 tests.${NC}"
        echo "Install K6: https://k6.io/docs/getting-started/installation/"
    fi
}

# Function to run JMeter tests
run_jmeter_tests() {
    echo -e "${YELLOW}🔧 Running JMeter tests...${NC}"
    
    if command -v jmeter &> /dev/null; then
        echo "Running JMeter basic load test..."
        mkdir -p "$RESULTS_DIR/jmeter"
        
        jmeter -n -t ./basic-load-test.jmx \
               -l "$RESULTS_DIR/jmeter/results-$TIMESTAMP.jtl" \
               -e -o "$RESULTS_DIR/jmeter/report-$TIMESTAMP"
        
        echo -e "${GREEN}✅ JMeter tests completed${NC}"
        echo "Report available at: $RESULTS_DIR/jmeter/report-$TIMESTAMP/index.html"
    else
        echo -e "${YELLOW}⚠️ JMeter not installed. Skipping JMeter tests.${NC}"
        echo "Install JMeter: https://jmeter.apache.org/download_jmeter.cgi"
    fi
}

# Function to run concurrent curl tests
run_concurrent_tests() {
    echo -e "${YELLOW}⚡ Running concurrent curl tests...${NC}"
    
    echo "Testing with 20 concurrent requests to fast endpoint..."
    for i in {1..20}; do
        curl -s -w "%{time_total}\n" -o /dev/null "$API_URL/api/performance/fast" &
    done
    wait
    
    echo -e "${GREEN}✅ Concurrent tests completed${NC}"
}

# Function to collect metrics
collect_metrics() {
    echo -e "${YELLOW}📊 Collecting performance metrics...${NC}"
    
    echo "Application metrics from /actuator/metrics:"
    curl -s "$API_URL/actuator/metrics" | jq '.names | sort' 2>/dev/null || echo "Metrics endpoint not available"
    
    echo -e "\nCustom performance metrics:"
    curl -s "$API_URL/api/performance/metrics" | jq . 2>/dev/null || echo "Performance metrics endpoint not available"
    
    echo -e "\nHTTP request metrics:"
    curl -s "$API_URL/actuator/metrics/http.server.requests" | jq . 2>/dev/null || echo "HTTP metrics not available"
    
    echo -e "\nJVM memory usage:"
    curl -s "$API_URL/actuator/metrics/jvm.memory.used" | jq . 2>/dev/null || echo "JVM metrics not available"
}

# Function to generate summary report
generate_report() {
    echo -e "${YELLOW}📋 Generating performance test summary...${NC}"
    
    REPORT_FILE="$RESULTS_DIR/performance-summary-$TIMESTAMP.md"
    
    cat > "$REPORT_FILE" << EOF
# Performance Test Report

**Date:** $(date)
**API URL:** $API_URL
**Test Duration:** $(date)

## Test Summary

### Basic Endpoint Tests
- Fast endpoint: ✅ Tested
- Slow endpoint: ✅ Tested  
- Variable complexity: ✅ Tested
- Memory stats: ✅ Tested

### Load Testing Tools Used
- K6: $(command -v k6 &> /dev/null && echo "✅ Available" || echo "❌ Not installed")
- JMeter: $(command -v jmeter &> /dev/null && echo "✅ Available" || echo "❌ Not installed")
- Curl: ✅ Available

## Results Location
- Results directory: \`$RESULTS_DIR\`
- K6 results: \`k6-results-$TIMESTAMP.json\`
- JMeter results: \`jmeter/report-$TIMESTAMP/index.html\`

## Metrics Endpoints
- Health: $API_URL/actuator/health
- Metrics: $API_URL/actuator/metrics
- Custom metrics: $API_URL/api/performance/metrics
- Prometheus: $API_URL/actuator/prometheus

## Next Steps
1. Review test results in the results directory
2. Check application logs for any errors
3. Monitor resource usage during peak load
4. Optimize endpoints with high response times

EOF

    echo -e "${GREEN}✅ Report generated: $REPORT_FILE${NC}"
    echo "Check the report for detailed results."
}

# Main execution
main() {
    check_service
    run_basic_tests
    run_concurrent_tests
    
    if [[ "${1:-all}" == "all" ]]; then
        run_k6_tests
        run_jmeter_tests
    elif [[ "$1" == "k6" ]]; then
        run_k6_tests
    elif [[ "$1" == "jmeter" ]]; then
        run_jmeter_tests
    fi
    
    collect_metrics
    generate_report
    
    echo -e "${GREEN}🎉 Performance testing completed!${NC}"
    echo "Check results in: $RESULTS_DIR"
}

# Help function
show_help() {
    echo "Usage: $0 [all|k6|jmeter|basic|help]"
    echo ""
    echo "Commands:"
    echo "  all     - Run all available tests (default)"
    echo "  k6      - Run only K6 tests"
    echo "  jmeter  - Run only JMeter tests"  
    echo "  basic   - Run only basic curl tests"
    echo "  help    - Show this help message"
    echo ""
    echo "Prerequisites:"
    echo "  - Orchid API running on $API_URL"
    echo "  - curl and jq installed"
    echo "  - K6 installed (optional)"
    echo "  - JMeter installed (optional)"
}

# Handle command line arguments
case "${1:-all}" in
    "help"|"-h"|"--help")
        show_help
        ;;
    "basic")
        check_service
        run_basic_tests
        collect_metrics
        generate_report
        ;;
    *)
        main "$1"
        ;;
esac
