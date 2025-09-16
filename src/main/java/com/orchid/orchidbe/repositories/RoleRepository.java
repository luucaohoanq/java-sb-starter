/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.repositories;

import com.orchid.orchidbe.domain.role.Role;
import com.orchid.orchidbe.domain.role.Role.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  boolean existsByName(RoleName name);

  Optional<Role> findByName(RoleName name);
}
