package com.orchid.orchidbe.domain.token;

import com.orchid.orchidbe.domain.account.Account;

public interface TokenService {

    Token addToken(Long userId, String token, boolean isMobileDevice);

    Token refreshToken(String refreshToken, Account user) throws Exception;

    void deleteToken(String token, Account user);

    Token findAccountByToken(String token);

}
