package com.orchid.orchidbe.domain.auth;

import com.orchid.orchidbe.annotations.auth.LoginApiResponses;
import com.orchid.orchidbe.annotations.auth.LoginOperation;
import com.orchid.orchidbe.annotations.auth.LogoutApiResponses;
import com.orchid.orchidbe.annotations.auth.LogoutOperation;
import com.orchid.orchidbe.annotations.auth.RegisterApiResponses;
import com.orchid.orchidbe.annotations.auth.RegisterOperation;
import com.orchid.orchidbe.apis.MyApiResponse;
import com.orchid.orchidbe.domain.account.AccountDTO;
import com.orchid.orchidbe.domain.auth.AuthPort.LoginResponse;
import com.orchid.orchidbe.domain.account.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "🔐 auth", description = """
    Authentication endpoints for login, logout, and token management.""")
public class AuthController {

    AccountService userService;
    AuthService authService;

    @LoginOperation
    @LoginApiResponses
    @PostMapping("/login")
    public ResponseEntity<MyApiResponse<LoginResponse>> login(
        @RequestBody @Valid
        AuthPort.LoginReq loginReq,
        HttpServletRequest request
    ) throws Exception {
        return MyApiResponse.success(authService.login(loginReq, request));
    }

    @PostMapping("/register")
    @RegisterOperation
    @RegisterApiResponses
    public ResponseEntity<MyApiResponse<Object>> createAccount(
        @RequestBody @Valid AccountDTO.CreateAccountReq accountReq
    ) {
        userService.add(accountReq);
        return MyApiResponse.created();
    }


    @LogoutOperation
    @LogoutApiResponses
    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_STAFF')")
    public ResponseEntity<MyApiResponse<Object>> logout(HttpServletRequest request) {
        authService.logout(request);
        return MyApiResponse.noContent();
    }
}