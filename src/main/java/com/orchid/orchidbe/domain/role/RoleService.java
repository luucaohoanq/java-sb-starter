package com.orchid.orchidbe.domain.role;

import com.orchid.orchidbe.domain.role.Role.RoleName;
import java.util.List;

public interface RoleService {

    List<Role> getAll();
    Role getById(Long id);
    Role getByName(RoleName name);
    void add(RoleDTO.RoleReq role);
    void update(Long id , RoleDTO.RoleReq role);
    void delete(Long id);

}
