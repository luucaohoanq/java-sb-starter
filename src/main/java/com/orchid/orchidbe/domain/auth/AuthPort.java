package com.orchid.orchidbe.domain.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.orchid.orchidbe.domain.account.AccountDTO;
import com.orchid.orchidbe.domain.token.TokenPort.TokenResponse;
import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.token.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AuthPort {

    @JsonInclude(Include.NON_NULL)
    record LoginResponse(
        TokenResponse token,
        AccountDTO.AccountCompactRes account
    ) {

        public static LoginResponse from(
            Token token,
            Account account
        ) {
            return new LoginResponse(
                new TokenResponse(
                    token.getId(),
                    token.getToken(),
                    token.getRefreshToken(),
                    token.getTokenType(),
                    token.getExpirationDate(),
                    token.getRefreshExpirationDate(),
                    token.isMobile(),
                    token.isRevoked(),
                    token.isExpired()
                ),
                new AccountDTO.AccountCompactRes(account.getId(),
                                                 account.getEmail(),
                                                 account.getRole().getName()));
        }
    }

    record LoginReq(
        @Email(message = "Email is not valid")
        @Schema(description = "User email", example = "mnhw.0612@gmail.com")
        String email,

        @NotBlank(message = "Password is required")
        @Schema(description = "User password", example = "string")
        String password
    ) {

    }

}
