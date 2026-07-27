package com.example.cinema_api.controller;


import com.example.cinema_api.dto.RoleRequest;
import com.example.cinema_api.dto.RoleResponse;
import com.example.cinema_api.service.IRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Roles API Operations")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest){
        RoleResponse responseRoleDTO = roleService.createRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseRoleDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequest roleRequest){
        RoleResponse responseRoleDTO = roleService.updateRole(id, roleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseRoleDTO);
    }

   @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> findAllRoles(){
        List<RoleResponse> responseRoleDTO = roleService.findAllRoles();
        return ResponseEntity.status(HttpStatus.OK).body(responseRoleDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @GetMapping("/role/{id}")
    public ResponseEntity<RoleResponse> findRoleById(@PathVariable Long id){
        RoleResponse responseRoleDTO = roleService.findRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseRoleDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
