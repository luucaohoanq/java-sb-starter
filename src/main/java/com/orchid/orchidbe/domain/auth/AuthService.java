package com.orchid.orchidbe.domain.auth;

import com.orchid.orchidbe.domain.auth.AuthPort.LoginReq;
import com.orchid.orchidbe.domain.token.TokenPort;
import com.orchid.orchidbe.domain.account.Account;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    Account getUserDetailsFromRefreshToken(String refreshToken) throws Exception;
    Account getUserDetailsFromToken(String token) throws Exception;
    AuthPort.LoginResponse login(LoginReq loginReq, HttpServletRequest request);
    AuthPort.LoginResponse refreshToken(TokenPort.RefreshTokenDTO refreshTokenDTO) throws Exception;
    void logout(HttpServletRequest request);

}
