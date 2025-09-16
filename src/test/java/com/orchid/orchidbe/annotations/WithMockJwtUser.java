/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.annotations;

import com.orchid.orchidbe.domain.role.Role.RoleName;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * Custom annotation for creating a mock JWT authenticated user in tests.
 * This annotation simplifies testing of secured endpoints by automatically
 * creating a mock authenticated user with specified roles.
 * 
 * Usage:
 * - @WithMockJwtUser - Creates a user with USER role and default email
 * - @WithMockJwtUser(roles = {RoleName.ADMIN}) - Creates an admin user
 * - @WithMockJwtUser(email = "custom@test.com", roles = {RoleName.MANAGER}) - Creates a custom user
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockJwtUserSecurityContextFactory.class)
public @interface WithMockJwtUser {
    
    /**
     * The email/username of the mock user
     */
    String email() default "test@example.com";
    
    /**
     * The name of the mock user
     */
    String name() default "Test User";
    
    /**
     * The roles to assign to the mock user
     */
    RoleName[] roles() default {RoleName.USER};
    
    /**
     * The user ID
     */
    long userId() default 1L;
}
