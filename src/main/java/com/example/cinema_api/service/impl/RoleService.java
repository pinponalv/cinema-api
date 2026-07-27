package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.PermissionIdRequest;
import com.example.cinema_api.dto.PermissionResponse;
import com.example.cinema_api.dto.RoleRequest;
import com.example.cinema_api.dto.RoleResponse;
import com.example.cinema_api.entity.Permission;
import com.example.cinema_api.entity.Roles;
import com.example.cinema_api.exception.ResourceNotFoundException;
import com.example.cinema_api.repository.PermissionRepository;
import com.example.cinema_api.repository.RolesRepository;
import com.example.cinema_api.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: CREAR METODO PARA NO TENER QUE TENER TANTO CODIGO REPETIDO

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RolesRepository rolesRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Roles roles = new Roles();
        roles.setRole(roleRequest.getRole());
        roles.setPermissionsList(resolvePermissions(roleRequest.getPermissions()));
        Roles savedRoles = rolesRepository.save(roles);

        return toRoleResponse(savedRoles);
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        Roles roles = rolesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if(roleRequest.getRole() != null) {
            roles.setRole(roleRequest.getRole());
        }
        if(roleRequest.getPermissions() != null) {
            roles.setPermissionsList(resolvePermissions(roleRequest.getPermissions()));
        }
        Roles savedRoles = rolesRepository.save(roles);

        return toRoleResponse(savedRoles);
    }

    @Override
    public List<RoleResponse> findAllRoles() {
        List<Roles> rolesList = rolesRepository.findAll();
        List<RoleResponse> roleResponseList = new ArrayList<>();

        for(Roles roles : rolesList) {
            roleResponseList.add(toRoleResponse(roles));
        }
        return roleResponseList;
    }

    @Override
    public RoleResponse findRoleById(Long id) {
        Roles roles = rolesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return toRoleResponse(roles);
    }

    @Override
    public void deleteRole(Long id) {
        if(!rolesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found");
        }
        rolesRepository.deleteById(id);
    }


    //TODO: documentar mejor este metodo
    private Set<Permission> resolvePermissions(Set<PermissionIdRequest> permissionIdRequests) {
        Set<Permission> permissions = new HashSet<>();
        for(PermissionIdRequest permission : permissionIdRequests ) {
            Permission readPermission = permissionRepository.findById(permission.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
            permissions.add(readPermission);
        }
        return permissions;
    }


    //TODO: documentar mejor este metodo
    private RoleResponse toRoleResponse(Roles roles) {
        Set<PermissionResponse> permissionsList = new HashSet<>();

        for(Permission permission : roles.getPermissionsList()) {
            PermissionResponse response = new PermissionResponse();
            response.setId(permission.getId());
            response.setPermission(permission.getPermissionName());
            permissionsList.add(response);
        }

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(roles.getId());
        roleResponse.setRole(roles.getRole());
        roleResponse.setPermissions(permissionsList);
        return roleResponse;
    }

}
