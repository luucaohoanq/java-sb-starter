# Security Testing with @WithMockJwtUser Annotation

This project includes a custom annotation `@WithMockJwtUser` for testing Spring Security endpoints with JWT authentication.

## Features

- **Custom Security Annotation**: `@WithMockJwtUser` for easy mock authentication in tests
- **Role-based Testing**: Test different user roles (USER, STAFF, MANAGER, ADMIN)
- **@PreAuthorize Testing**: Comprehensive tests for Spring Security method-level security

## Usage

### Basic Usage

```java
@Test
@WithMockJwtUser  // Creates a user with USER role and default email
void testWithDefaultUser() {
    // Your test code here
}
```

### Custom Roles

```java
@Test
@WithMockJwtUser(roles = {RoleName.ADMIN})
void testWithAdminUser() {
    // Test admin-only endpoints
}

@Test
@WithMockJwtUser(roles = {RoleName.MANAGER})
void testWithManagerUser() {
    // Test manager-level access
}
```

### Custom User Details

```java
@Test
@WithMockJwtUser(
    email = "custom@test.com",
    name = "Custom User",
    roles = {RoleName.STAFF},
    userId = 123L
)
void testWithCustomUser() {
    // Test with specific user details
}
```

## Available Roles

- `RoleName.USER` - Regular user
- `RoleName.STAFF` - Staff member
- `RoleName.MANAGER` - Manager level
- `RoleName.ADMIN` - Administrator

## Security Test Structure

The security tests are organized into nested test classes for better organization:

```java
@Nested
@DisplayName("GET /api/accounts - Security Tests")
class GetAccountsSecurityTests {

    @Test
    @DisplayName("Should deny access when no authentication")
    void getAccounts_shouldReturn401_whenNotAuthenticated() { }

    @Test
    @WithMockJwtUser(roles = {RoleName.ADMIN})
    @DisplayName("Should allow access for ADMIN role")
    void getAccounts_shouldReturn200_whenAdminRole() { }
}
```

## Test Scenarios Covered

### GET /api/accounts

- ✅ Unauthorized access (401)
- ✅ USER role access (403 Forbidden)
- ✅ STAFF role access (403 Forbidden)
- ✅ ADMIN role access (200 OK)
- ✅ MANAGER role access (200 OK)

### POST /api/accounts/create-new-employee

- ✅ Unauthorized access (401)
- ✅ USER role access (403 Forbidden)
- ✅ STAFF role access (403 Forbidden)
- ✅ ADMIN role access (201 Created)
- ✅ MANAGER role access (201 Created)

## Running the Tests

```bash
# Run all security tests
./mvnw test -Dtest=AccountControllerSecurityTest

# Run specific test class
./mvnw test -Dtest=AccountControllerSecurityTest.GetAccountsSecurityTests

# Run with verbose output
./mvnw test -Dtest=AccountControllerSecurityTest -Dspring.logging.level.org.springframework.security=DEBUG
```

## Implementation Details

### @WithMockJwtUser Annotation

- Uses Spring Security's `@WithSecurityContext` for test authentication
- Automatically creates mock `Account` objects with specified roles
- Integrates seamlessly with `@PreAuthorize` annotations

### Security Context Factory

- `WithMockJwtUserSecurityContextFactory` creates the security context
- Builds realistic `Account` objects for testing
- Handles role-based authorities correctly

### Test Configuration

- `TestSecurityConfig` enables method security for tests
- Ensures `@PreAuthorize` annotations are properly enforced
- Maintains security behavior in test environment

## Best Practices

1. **Use Descriptive Test Names**: Include role and expected outcome
2. **Test All Roles**: Verify both allowed and forbidden access
3. **Test Unauthorized Access**: Always test without authentication
4. **Use Nested Classes**: Group related security tests together
5. **Mock External Dependencies**: Use `@MockBean` for services

## Example Test Class

```java
@IntegrationTest
@AutoConfigureMockMvc
@DisplayName("Account Controller Security Tests")
public class AccountControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Nested
    @DisplayName("GET /api/accounts - Security Tests")
    class GetAccountsSecurityTests {
        // Security tests here
    }
}
```
