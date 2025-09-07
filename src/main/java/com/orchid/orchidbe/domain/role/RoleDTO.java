package com.orchid.orchidbe.domain.role;

import com.orchid.orchidbe.domain.role.Role.RoleName;

public interface RoleDTO {

    record RoleReq(
        RoleName name
    ) {

    }


}
