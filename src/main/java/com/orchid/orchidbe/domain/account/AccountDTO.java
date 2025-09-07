package com.orchid.orchidbe.domain.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orchid.orchidbe.domain.role.Role;
import com.orchid.orchidbe.domain.role.Role.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AccountDTO {

    record CreateAccountReq(
        String name,

        @Email(message = "Email is not valid")
        @NotBlank(message = "Email is required")
        @Schema(description = "User email", example = "mnhw.0612@gmail.com")
        String email,

        String password
    ) {

    }

    record CreateStaffReq(
        String name,

        @Email(message = "Email is not valid")
        @NotBlank(message = "Email is required")
        @Schema(description = "User email", example = "mnhw.0612@gmail.com")
        String email
    ) {

    }

    record UpdateAccountReq(
        String name,
        String email,
        String password,
        RoleName roleName
    ) {

    }

    record AccountResp(
        Long id,
        String name,
        String email,
        @JsonProperty(value = "role_name")
        RoleName roleName
    ) {

        public static AccountResp fromEntity(
            Account account
        ) {
            return new AccountResp(account.getId(), account.getName(),
                                   account.getEmail(), account.getRole().getName());
        }

    }

    record AccountCompactRes(
        Long id,
        String email,
        RoleName role
    ){
        public static AccountCompactRes fromEntity(
            Account account
        ){
            return new AccountCompactRes(account.getId(), account.getEmail(),
                                         account.getRole().getName());
        }
    }

}
