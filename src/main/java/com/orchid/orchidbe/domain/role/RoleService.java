/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.role;

import com.orchid.orchidbe.domain.role.Role.RoleName;
import java.util.List;

public interface RoleService {

    List<Role> getAll();

    Role getById(Long id);

    Role getByName(RoleName name);

    void add(RoleDTO.RoleReq role);

    void update(Long id, RoleDTO.RoleReq role);

    void delete(Long id);
}
