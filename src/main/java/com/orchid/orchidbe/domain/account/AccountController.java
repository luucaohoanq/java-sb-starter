package com.orchid.orchidbe.domain.account;

import com.orchid.orchidbe.apis.MyApiResponse;
import com.orchid.orchidbe.domain.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/accounts")
@RequiredArgsConstructor
@Tag(name = "accounts", description = "Operation related to Account")
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final AuthService authService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get all accounts", description = "Returns a list of all accounts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all accounts")
    public ResponseEntity<MyApiResponse<List<AccountDTO.AccountResp>>> getAccounts() {
        return MyApiResponse.success(accountService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_STAFF', 'ROLE_MANAGER')")
    @Operation(summary = "Get all accounts", description = "Returns a list of all accounts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all accounts")
    public ResponseEntity<MyApiResponse<Account>> getAccountById(@PathVariable Long id) {
        return MyApiResponse.success(accountService.getById(id));
    }

    //Get user details from token
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<MyApiResponse<Account>> getUserDetails(HttpServletRequest request)
        throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return MyApiResponse.unauthorized("Missing or invalid Authorization header");
        }
        token = token.substring(7); // Remove "Bearer " prefix
        Account userDetail = authService.getUserDetailsFromToken(token);
        return MyApiResponse.success(userDetail);
    }

    @PostMapping("/create-new-employee")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create new employee", description = "Creates a new employee account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    public ResponseEntity<MyApiResponse<Object>> createNewEmployee(
        @RequestBody AccountDTO.CreateStaffReq accountReq
    ) {
        accountService.addEmployee(accountReq);
        return MyApiResponse.created();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update account", description = "Updates an existing account by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<MyApiResponse<Object>> updateAccount(
        @PathVariable Long id,
        @RequestBody AccountDTO.UpdateAccountReq accountReq
    ) {
        accountService.update(id, accountReq);
        return MyApiResponse.updated();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Delete account", description = "Deletes an account by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<MyApiResponse<Object>> deleteAccount(@PathVariable Long id) {
        accountService.delete(id);
        return MyApiResponse.noContent();
    }

}
