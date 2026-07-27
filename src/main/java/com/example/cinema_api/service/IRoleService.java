package com.example.cinema_api.service;

import com.example.cinema_api.dto.RoleRequest;
import com.example.cinema_api.dto.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse updateRole(Long id,RoleRequest roleRequest);
    List<RoleResponse> findAllRoles();
    RoleResponse findRoleById(Long id);
    void deleteRole(Long id);
}
