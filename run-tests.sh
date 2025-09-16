#!/bin/bash

# Test runner script for AccountControllerIT
cd /mnt/26a0de97-0abd-45e1-b610-129d3045f430/project/repo-templates/java-sb-starter

echo "🧪 Running integration tests for AccountControllerIT..."
echo "=================================================="

# Run the specific failing tests
echo "📝 Running: getAccounts_shouldReturnEmptyList_whenNoAccounts"
./mvnw test -Dtest=AccountControllerIT#getAccounts_shouldReturnEmptyList_whenNoAccounts -q

echo "📝 Running: getAccounts_shouldReturnListOfAccounts_whenAccountsExist"
./mvnw test -Dtest=AccountControllerIT#getAccounts_shouldReturnListOfAccounts_whenAccountsExist -q

echo "📝 Running: createNewEmployee_shouldReturnEmployee_whenValid"
./mvnw test -Dtest=AccountControllerIT#createNewEmployee_shouldReturnEmployee_whenValid -q

echo "🎯 Running all AccountControllerIT tests..."
./mvnw test -Dtest=AccountControllerIT -q

echo "✅ Test execution completed!"
