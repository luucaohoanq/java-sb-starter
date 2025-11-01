/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.annotations;

import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.account.UserEnum.Status;
import com.orchid.orchidbe.domain.role.Role;
import com.orchid.orchidbe.domain.role.Role.RoleName;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * Security context factory for creating mock JWT authenticated users in tests. This factory is used
 * by the @WithMockJwtUser annotation to create a mock authentication context with the specified
 * user details and roles.
 */
public class WithMockJwtUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockJwtUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Create the mock user account
        Account mockAccount =
                Account.builder()
                        .id(annotation.userId())
                        .email(annotation.email())
                        .name(annotation.name())
                        .password("mock_password") // Password is not important for tests
                        .status(Status.VERIFIED)
                        .role(createMockRole(annotation.roles()[0])) // Use first role as primary
                        .build();

        // Create authentication token
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        mockAccount, null, mockAccount.getAuthorities());

        context.setAuthentication(authentication);
        return context;
    }

    /** Create a mock Role object with the specified role name */
    private Role createMockRole(RoleName roleName) {
        return Role.builder().id(1L).name(roleName).build();
    }
}
