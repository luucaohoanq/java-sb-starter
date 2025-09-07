package com.orchid.orchidbe.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Orchid Service API",
        description = """
            ## Authentication Guide

            ### Quick Login Process:
            1. **Use the Login Endpoint**: Navigate to `POST /api/v1/auth/login`
            2. **Try it out**: Click "Try it out" button
            3. **Enter Credentials**: Fill in your email and password
            4. **Execute**: Click "Execute" to get your token
            5. **Copy Token**: Copy the `token` value from the response
            6. **Authorize**: Click the 🔒 "Authorize" button at the top
            7. **Paste Token**: Enter `Bearer YOUR_TOKEN_HERE` in the value field
            8. **Apply**: Click "Authorize" and then "Close"

            ### Example Login Request:
            ```json
            {
              "email": "user@example.com",
              "password": "yourpassword"
            }
            ```

            ### Token Format:
            The authorization header should be: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

            **Note**: Tokens expire based on the `expirationDate` in the login response.
            """,
        version = "1.0",
        contact = @Contact(
            name = "API Support",
            email = "support@orchid.com"
        )
    ),
    security = @SecurityRequirement(name = "bearer-jwt"),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local Dev (HTTP)"),
        @Server(url = "https://api.example.com", description = "Production (HTTPS)")
    }
)
@SecurityScheme(
    name = "bearer-jwt",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER,
    bearerFormat = "JWT",
    description = """
        **JWT Authentication**

        To authenticate:
        1. Login via POST /api/v1/auth/login
        2. Copy the token from response
        3. Click 'Authorize' and enter: Bearer YOUR_TOKEN

        Token format: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
        """
)
public class OpenAPIConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                      .title("Orchid Service API")
                      .version("1.0")
                      .description("""
                    # Authentication Instructions

                    ## Step-by-Step Authentication:

                    ### 1. Login First
                    - Go to **Auth Controller** → **POST /auth/login**
                    - Click **"Try it out"**
                    - Enter your credentials in the request body
                    - Click **"Execute"**

                    ### 2. Get Your Token
                    - From the response, copy the **token** value (not the entire response)
                    - It should look like: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

                    ### 3. Authorize
                    - Click the **🔒 Authorize** button (top right)
                    - In the "Value" field, enter: `Bearer YOUR_COPIED_TOKEN`
                    - Click **"Authorize"** then **"Close"**

                    ### 4. Test Protected Endpoints
                    - Now you can test any protected endpoint
                    - The token will be automatically included in requests

                    ---

                    **Tip**: Keep the login tab open to easily copy the token when it expires!
                    """)
                      .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
            );
    }
}
