/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.annotations.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "🚪 Logout and invalidate token",
        description =
                """
            **Logout and invalidate your current JWT token**

            This endpoint requires authentication. Make sure you're logged in first.
            After logout, you'll need to login again to access protected endpoints.
            """,
        security = @SecurityRequirement(name = "bearer-jwt"))
public @interface LogoutOperation {}
