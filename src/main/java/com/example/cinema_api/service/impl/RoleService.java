package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.PermissionIdRequest;
import com.example.cinema_api.dto.PermissionResponse;
import com.example.cinema_api.dto.RoleRequest;
import com.example.cinema_api.dto.RoleResponse;
import com.example.cinema_api.entity.Permission;
import com.example.cinema_api.entity.Roles;
import com.example.cinema_api.repository.PermissionRepository;
import com.example.cinema_api.repository.RolesRepository;
import com.example.cinema_api.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: ARREGLAR AQUI COSAS

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RolesRepository rolesRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Set<Permission> permissionsList = new HashSet<>();
        Permission readPermission;

        for (PermissionIdRequest permission : roleRequest.getPermissions()) {
            readPermission = permissionRepository.findById(permission.getId()).orElseThrow(() -> new RuntimeException("Permission not found"));
            permissionsList.add(readPermission);
        }

        Roles roles = new Roles();
        roles.setRole(roleRequest.getRole());
        roles.setPermissionsList(permissionsList);
        Roles savedRoles = rolesRepository.save(roles);

        Set<PermissionResponse> savedPermissionsList = new HashSet<>();

        for(Permission permission : savedRoles.getPermissionsList()) {
            PermissionResponse response = new PermissionResponse();
            response.setId(permission.getId());
            response.setPermission(permission.getPermissionName());
            savedPermissionsList.add(response);
        }
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(savedRoles.getId());
        roleResponse.setRole(savedRoles.getRole());
        roleResponse.setPermissions(savedPermissionsList);

        return roleResponse;
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        Roles roles = rolesRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

        if(roleRequest.getRole() != null) {
            roles.setRole(roleRequest.getRole());
        }
        Roles savedRoles = rolesRepository.save(roles);

        Set<PermissionResponse> permissionsListt = new HashSet<>();
        for(Permission permission : savedRoles.getPermissionsList()) {
            PermissionResponse response = new PermissionResponse();
            response.setId(permission.getId());
            response.setPermission(permission.getPermissionName());
            permissionsListt.add(response);
        }

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(savedRoles.getId());
        roleResponse.setRole(savedRoles.getRole());
        roleResponse.setPermissions(permissionsListt);

        return roleResponse;
    }

    @Override
    public List<RoleResponse> findAllRoles() {
        List<Roles> rolesList = rolesRepository.findAll();
        List<RoleResponse> roleResponseList = new ArrayList<>();
        for (Roles roles : rolesList) {
            Set<PermissionResponse> permissionsList = new HashSet<>();

            for (Permission permission : roles.getPermissionsList()) {
                PermissionResponse response = new PermissionResponse();
                response.setId(permission.getId());
                response.setPermission(permission.getPermissionName());
                permissionsList.add(response);
            }
            RoleResponse roleResponse = new RoleResponse();
            roleResponse.setId(roles.getId());
            roleResponse.setRole(roles.getRole());
            roleResponse.setPermissions(permissionsList);

            roleResponseList.add(roleResponse);

        }
        return roleResponseList;
    }

    @Override
    public RoleResponse findRoleById(Long id) {
        Roles roles = rolesRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        Set<PermissionResponse> permissionsList = new HashSet<>();

        for(Permission p:  roles.getPermissionsList()) {
            PermissionResponse response = new PermissionResponse();
            response.setId(p.getId());
            response.setPermission(p.getPermissionName());
            permissionsList.add(response);
        }

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(roles.getId());
        roleResponse.setRole(roles.getRole());
        roleResponse.setPermissions(permissionsList);
        return roleResponse;
    }

    @Override
    public void deleteRole(Long id) {
        rolesRepository.deleteById(id);
    }
}
