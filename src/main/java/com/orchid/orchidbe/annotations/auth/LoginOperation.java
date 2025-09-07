package com.orchid.orchidbe.annotations.auth;

import com.orchid.orchidbe.domain.account.LoginReq;
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
    summary = "🚀 Login to get JWT token",
    description = """
            **Login and get your authentication token**
            
            ### How to use this token in Swagger:
            1. **Execute this login request** with your credentials
            2. **Copy the `token` value** from the response (the long string starting with 'eyJ...')
            3. **Click the 🔒 Authorize button** at the top of this page
            4. **Enter:** `Bearer YOUR_COPIED_TOKEN` (include the word "Bearer" and a space)
            5. **Click Authorize** and then Close
            6. **You're now authenticated!** Try any protected endpoint.
            
            ### Example Response:
            The response will contain a `token` field - this is what you need to copy for authorization.
            
            ### Token expires based on the `expirationDate` in the response.
            """,
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Login credentials",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LoginReq.class),
            examples = {
                @ExampleObject(
                    name = "Demo User",
                    summary = "Example login",
                    description = "Sample credentials for testing",
                    externalValue = "classpath:/swagger/examples/login.json"
                )
            }
        )
    )
)
public @interface LoginOperation {

}
