/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.auth;

import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.auth.AuthPort.LoginReq;
import com.orchid.orchidbe.domain.token.TokenPort;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  Account getUserDetailsFromRefreshToken(String refreshToken) throws Exception;

  Account getUserDetailsFromToken(String token) throws Exception;

  AuthPort.LoginResponse login(LoginReq loginReq, HttpServletRequest request);

  AuthPort.LoginResponse refreshToken(TokenPort.RefreshTokenDTO refreshTokenDTO) throws Exception;

  void logout(HttpServletRequest request);
}
