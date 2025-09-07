package com.orchid.orchidbe.domain.auth;

import com.orchid.orchidbe.components.JwtTokenUtils;
import com.orchid.orchidbe.domain.account.AccountService;
import com.orchid.orchidbe.domain.auth.AuthPort.LoginReq;
import com.orchid.orchidbe.domain.auth.AuthPort.LoginResponse;
import com.orchid.orchidbe.domain.token.TokenPort.RefreshTokenDTO;
import com.orchid.orchidbe.exceptions.TokenNotFoundException;
import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.token.Token;
import com.orchid.orchidbe.repositories.AccountRepository;
import com.orchid.orchidbe.repositories.TokenRepository;
import com.orchid.orchidbe.domain.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountService accountService;
    private final TokenService tokenService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    @Override
    public LoginResponse login(LoginReq loginReq, HttpServletRequest request) {
        log.info("Login body received: {}", loginReq);
        String email = loginReq.email();
        String password = loginReq.password();
        String token = null;

        Optional<Account> optionalUser = accountRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
        }

        Account existingUser = optionalUser.get();

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password,
                                                    existingUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        token = jwtTokenUtils.generateToken(existingUser);

        String userAgent = request.getHeader("User-Agent");
        Account userDetail = getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail.getId(), token,
                                               isMobileDevice(userAgent));

        log.info("User logged in successfully");
        return AuthPort.LoginResponse.from(jwtToken, userDetail);
    }

    @Override
    public Account getUserDetailsFromToken(String token) {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is expired");
        }
        String email = jwtTokenUtils.extractEmail(token);
        Optional<Account> user = accountRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return user.get();
    }

    @Override
    public Account getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new TokenNotFoundException("Refresh token does not exist"));
        return getUserDetailsFromToken(existingToken.getToken());
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception {
        Account userDetail = getUserDetailsFromRefreshToken(
            refreshTokenDTO.refreshToken());
        Token jwtToken = tokenService.refreshToken(refreshTokenDTO.refreshToken(), userDetail);
        return AuthPort.LoginResponse.from(jwtToken, userDetail);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = jwtTokenUtils.extractBearerToken(request);
        if (token == null) {
            throw new TokenNotFoundException("Token not found");
        }

        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is expired");
        }

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        var user = accountService.getByEmail(userDetails.getUsername());

        tokenService.deleteToken(token, user);
    }

    private boolean isMobileDevice(String userAgent) {
        // Kiểm tra User-Agent header để xác định thiết bị di động
        if (userAgent == null) {
            return false;
        }
        return userAgent.toLowerCase().contains("mobile");
    }
}
