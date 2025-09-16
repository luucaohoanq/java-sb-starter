/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.account.AccountDTO;
import com.orchid.orchidbe.domain.account.AccountServiceImpl;
import com.orchid.orchidbe.domain.role.Role;
import com.orchid.orchidbe.domain.role.Role.RoleName;
import com.orchid.orchidbe.domain.role.RoleService;
import com.orchid.orchidbe.repositories.AccountRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock private AccountRepository accountRepository;
  @Mock private RoleService roleService;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private AccountServiceImpl accountService;

  private List<Role> roles;

  @BeforeEach
  void init() {
    roles =
        List.of(
            new Role(1L, RoleName.USER),
            new Role(2L, RoleName.STAFF),
            new Role(3L, RoleName.ADMIN));
  }

  @Test
  void getAll_ShouldReturnListOfAccounts() {
    var user1 = new Account();
    user1.setRole(roles.get(0));
    var user2 = new Account();
    user2.setRole(roles.get(1));
    var user3 = new Account();
    user3.setRole(roles.get(2));

    when(accountRepository.findAll()).thenReturn(List.of(user1, user2, user3));

    var result = accountService.getAll();

    assert (result.size() == 3);
  }

  @Test
  void add_ShouldSaveAccountWithUserRole() {
    var req = new AccountDTO.CreateAccountReq("hoang", "hoang@gmail.com", "123456");
    when(accountRepository.existsByEmail(req.email())).thenReturn(false);
    when(passwordEncoder.encode("123456")).thenReturn("encoded_pw");
    when(roleService.getByName(RoleName.USER)).thenReturn(roles.getFirst());

    accountService.add(req);

    verify(accountRepository).save(any(Account.class));
  }
}
