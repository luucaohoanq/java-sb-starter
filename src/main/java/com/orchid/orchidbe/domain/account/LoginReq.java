package com.orchid.orchidbe.domain.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginReq(
    @Email(message = "Email is not valid")
    @Schema(description = "User email", example = "mnhw.0612@gmail.com")
    String email,

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "string")
    String password
) {

}