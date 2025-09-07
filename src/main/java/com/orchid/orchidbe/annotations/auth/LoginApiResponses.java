package com.orchid.orchidbe.annotations.auth;

import com.orchid.orchidbe.apis.MyApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "✅ Login successful - Copy the token value for authorization!",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = MyApiResponse.class),
            examples = @ExampleObject(
                name = "Success Response",
                description = "Copy the 'token' value and use it with 'Bearer ' prefix in the Authorize button",
                value = """
                        {
                          "code": 200,
                          "message": "Success",
                          "data": {
                            "tokenResponse": {
                              "id": "12345",
                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                              "refreshToken": "refresh_token_here",
                              "tokenType": "Bearer",
                              "expirationDate": "2024-12-31T23:59:59",
                              "refreshExpirationDate": "2025-01-31T23:59:59",
                              "mobile": false,
                              "revoked": false,
                              "expired": false
                            }
                          }
                        }
                        """
            )
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "❌ Invalid credentials"
    ),
    @ApiResponse(
        responseCode = "500",
        description = "❌ Server error"
    )
})
public @interface LoginApiResponses {

}
