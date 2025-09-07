package com.orchid.orchidbe.domain.role;

import com.orchid.orchidbe.apis.MyApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
@Tag(name = "roles", description = "Operation related to Role")
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<MyApiResponse<List<Role>>> getAll() {
        return MyApiResponse.success(roleService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<MyApiResponse<Role>> getById(@PathVariable Long id) {
        return MyApiResponse.success(roleService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    @Operation(
        summary = "Create a new role",
        description = """
            ** Create a new role **
            
            This endpoint allows you to create a new role in the system. The role must be one of the predefined values: STAFF, USER, MANAGER, ADMIN.
            
            **Note:** Ensure that the role name is one of the accepted values.
            """
    )
    public ResponseEntity<MyApiResponse<Void>> add(
        @Valid @RequestBody RoleDTO.RoleReq req
    ) {
        roleService.add(req);
        return MyApiResponse.created();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RoleDTO.RoleReq req) {
        roleService.update(id, req);
        return MyApiResponse.updated();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        roleService.delete(id);
        return MyApiResponse.noContent();
    }
}