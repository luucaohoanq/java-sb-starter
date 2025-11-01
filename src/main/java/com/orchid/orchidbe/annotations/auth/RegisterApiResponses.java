/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
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
@ApiResponses(
        value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "✅ Account created successfully",
                    content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MyApiResponse.class),
                                    examples =
                                            @ExampleObject(
                                                    name = "Success Response",
                                                    value =
                                                            """
                    {
                      "code": 201,
                      "message": "Created",
                      "data": null
                    }
                    """))),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌ Email already exists",
                    content =
                            @Content(
                                    mediaType = "application/json",
                                    examples =
                                            @ExampleObject(
                                                    value =
                                                            """
                    {
                      "code": 400,
                      "message": "Email already exists",
                      "data": null
                    }
                    """)))
        })
public @interface RegisterApiResponses {}
