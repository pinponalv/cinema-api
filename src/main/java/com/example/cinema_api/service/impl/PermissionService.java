package com.example.cinema_api.service.impl;


import com.example.cinema_api.dto.PermissionRequest;
import com.example.cinema_api.dto.PermissionResponse;
import com.example.cinema_api.entity.Permission;
import com.example.cinema_api.repository.PermissionRepository;
import com.example.cinema_api.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {
   private final PermissionRepository permissionRepository;

    @Override
    public PermissionResponse createPermission(PermissionRequest permission) {
        Permission newPermission = new Permission();
        newPermission.setPermissionName(permission.getPermission());

        Permission savePermission = permissionRepository.save(newPermission);
        return new PermissionResponse(
                savePermission.getId(),
                savePermission.getPermissionName()
        );
    }

    @Override
    public PermissionResponse updatePermission(Long id, PermissionRequest permission) {
        Permission getPermission = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
        getPermission.setPermissionName(permission.getPermission());
        Permission savePermission = permissionRepository.save(getPermission);

        return new PermissionResponse(
                savePermission.getId(),
                savePermission.getPermissionName()
        );
    }

    @Override
    public List<PermissionResponse> findAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();

        List<PermissionResponse> permissionDTOS = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionResponse permissionDTO = new PermissionResponse(
                    permission.getId(),
                    permission.getPermissionName()
            );
            permissionDTOS.add(permissionDTO);
        }
        return permissionDTOS;
    }

    @Override
    public void deletePermission(Long id) {
        if(!permissionRepository.existsById(id)){
            throw new RuntimeException("Permiso no encontrado");
        }
        //Permission getPermission = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
        permissionRepository.deleteById(id);
    }
}
