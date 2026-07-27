package com.example.cinema_api.service;

import com.example.cinema_api.dto.PermissionRequest;
import com.example.cinema_api.dto.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest permission);
    PermissionResponse updatePermission(Long id, PermissionRequest permission);
    List<PermissionResponse> findAllPermissions();
    void deletePermission(Long id);
}
