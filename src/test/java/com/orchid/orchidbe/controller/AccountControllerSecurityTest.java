/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchid.orchidbe.IntegrationTest;
import com.orchid.orchidbe.annotations.WithMockJwtUser;
import com.orchid.orchidbe.domain.account.AccountDTO;
import com.orchid.orchidbe.domain.account.AccountService;
import com.orchid.orchidbe.domain.auth.AuthService;
import com.orchid.orchidbe.domain.role.Role.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Security tests for AccountController endpoints.
 * Tests the @PreAuthorize annotations to ensure proper access control.
 */
@IntegrationTest
@AutoConfigureMockMvc
@DisplayName("Account Controller Security Tests")
public class AccountControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AuthService authService;

    @Nested
    @DisplayName("GET /api/accounts - Security Tests")
    class GetAccountsSecurityTests {

        @Test
        @DisplayName("Should deny access when no authentication")
        void getAccounts_shouldReturn401_whenNotAuthenticated() throws Exception {
            mockMvc.perform(get("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockJwtUser(roles = {RoleName.USER})
        @DisplayName("Should deny access for USER role")
        void getAccounts_shouldReturn403_whenUserRole() throws Exception {
            mockMvc.perform(get("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockJwtUser(roles = {RoleName.STAFF})
        @DisplayName("Should deny access for STAFF role")
        void getAccounts_shouldReturn403_whenStaffRole() throws Exception {
            mockMvc.perform(get("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockJwtUser(roles = {RoleName.ADMIN})
        @DisplayName("Should allow access for ADMIN role")
        void getAccounts_shouldReturn200_whenAdminRole() throws Exception {
            mockMvc.perform(get("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockJwtUser(roles = {RoleName.MANAGER})
        @DisplayName("Should allow access for MANAGER role")
        void getAccounts_shouldReturn200_whenManagerRole() throws Exception {
            mockMvc.perform(get("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("POST /api/accounts/create-new-employee - Security Tests")
    class CreateEmployeeSecurityTests {

        private final AccountDTO.CreateStaffReq validRequest = 
            new AccountDTO.CreateStaffReq("Test Employee", "employee@test.com");

        @Test
        @DisplayName("Should deny access when no authentication")
        void createEmployee_shouldReturn401_whenNotAuthenticated() throws Exception {
            mockMvc.perform(post("/api/accounts/create-new-employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockJwtUser(roles = {RoleName.USER})
        @DisplayName("Should deny access for USER role")
        void createEmployee_shouldReturn403_whenUserRole() throws Exception {
            mockMvc.perform(post("/api/accounts/create-new-employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockJwtUser(roles = {RoleName.STAFF})
        @DisplayName("Should deny access for STAFF role")
        void createEmployee_shouldReturn403_whenStaffRole() throws Exception {
            mockMvc.perform(post("/api/accounts/create-new-employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockJwtUser(roles = {RoleName.ADMIN})
        @DisplayName("Should allow access for ADMIN role")
        void createEmployee_shouldReturn201_whenAdminRole() throws Exception {
            mockMvc.perform(post("/api/accounts/create-new-employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
        }

        @Test
        @WithMockJwtUser(roles = {RoleName.MANAGER})
        @DisplayName("Should allow access for MANAGER role")
        void createEmployee_shouldReturn201_whenManagerRole() throws Exception {
            mockMvc.perform(post("/api/accounts/create-new-employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
        }
    }
}
