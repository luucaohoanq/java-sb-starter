/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.token;

import com.orchid.orchidbe.domain.account.Account;

public interface TokenService {

  Token addToken(Long userId, String token, boolean isMobileDevice);

  Token refreshToken(String refreshToken, Account user) throws Exception;

  void deleteToken(String token, Account user);

  Token findAccountByToken(String token);
}
