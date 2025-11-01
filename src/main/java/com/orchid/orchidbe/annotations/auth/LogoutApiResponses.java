/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.annotations.auth;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "✅ Logout successful"),
            @ApiResponse(
                    responseCode = "401",
                    description = "❌ Unauthorized - Token invalid or missing"),
            @ApiResponse(responseCode = "404", description = "❌ Token not found")
        })
public @interface LogoutApiResponses {}
