package com.example.cinema_api.controller;


import com.example.cinema_api.dto.RoleRequest;
import com.example.cinema_api.dto.RoleResponse;
import com.example.cinema_api.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Register role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest){
        RoleResponse responseRoleDTO = roleService.createRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseRoleDTO);
    }


    @Operation(summary = "Update role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequest roleRequest){
        RoleResponse responseRoleDTO = roleService.updateRole(id, roleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseRoleDTO);
    }

    @Operation(summary = "Find all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "204", description = "Not content"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
   @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> findAllRoles(){
        List<RoleResponse> responseRoleDTO = roleService.findAllRoles();
        return ResponseEntity.status(HttpStatus.OK).body(responseRoleDTO);
    }

    @Operation(summary = "Find role by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @GetMapping("/role/{id}")
    public ResponseEntity<RoleResponse> findRoleById(@PathVariable Long id){
        RoleResponse responseRoleDTO = roleService.findRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseRoleDTO);
    }

    @Operation(summary = "Delete a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Not content"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
