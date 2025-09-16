/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.account;

import java.util.List;

public interface AccountService {

  List<AccountDTO.AccountResp> getAll();

  Account getById(Long id);

  Account getByEmail(String email);

  void addEmployee(AccountDTO.CreateStaffReq account);

  void add(AccountDTO.CreateAccountReq account);

  void update(Long id, AccountDTO.UpdateAccountReq account);

  void delete(Long id);
}
