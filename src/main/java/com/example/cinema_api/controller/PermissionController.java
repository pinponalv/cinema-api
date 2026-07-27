package com.example.cinema_api.controller;

import com.example.cinema_api.dto.PermissionRequest;
import com.example.cinema_api.dto.PermissionResponse;
import com.example.cinema_api.service.impl.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@Tag(name = "Permissions", description = "Permissions API Operations")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(@Valid  @RequestBody PermissionRequest permissionRequest) {
        PermissionResponse response = permissionService.createPermission(permissionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long id, @Valid  @RequestBody PermissionRequest permissionRequest) {
        PermissionResponse response = permissionService.updatePermission(id, permissionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @GetMapping
    public ResponseEntity<List<PermissionResponse>> findAllPermissions() {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.findAllPermissions());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<PermissionResponse> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
