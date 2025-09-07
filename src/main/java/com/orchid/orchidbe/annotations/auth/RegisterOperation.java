package com.orchid.orchidbe.annotations.auth;

import com.orchid.orchidbe.domain.account.AccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "👤 Register new account",
    description = """
        **Create a new user account**
        
        This endpoint allows new users to register in the system.
        
        ### Registration process:
        1. Submit your account details including email and password
        2. After successful registration, use the login endpoint to get a token
        3. Your account will be created with default user permissions
        
        ### Password requirements:
        - Minimum 8 characters
        - Must include at least one number
        - Must include at least one letter
        """,
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Account registration details",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AccountDTO.CreateAccountReq.class),
            examples = {
                @ExampleObject(
                    name = "New User",
                    summary = "Example registration",
                    description = "Sample account registration data",
                    externalValue = "classpath:/swagger/examples/register.json"
                )
            }
        )
    )
)
public @interface RegisterOperation {
}

