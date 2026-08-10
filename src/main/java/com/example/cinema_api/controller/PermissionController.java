package com.example.cinema_api.controller;

import com.example.cinema_api.dto.PermissionRequest;
import com.example.cinema_api.dto.PermissionResponse;
import com.example.cinema_api.service.IPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    @Operation(
            summary = "Register permission",
            description = "Crea un nuevo permiso. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(@Valid  @RequestBody PermissionRequest permissionRequest) {
        PermissionResponse response = permissionService.createPermission(permissionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update permission",
            description = "Actualiza el nombre de un permiso existente por id. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long id, @Valid  @RequestBody PermissionRequest permissionRequest) {
        PermissionResponse response = permissionService.updatePermission(id, permissionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Find all permission",
            description = "Retorna la lista completa de permisos, sin paginación. Requiere rol ADMIN o MOD."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PermissionResponse>> findAllPermissions() {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.findAllPermissions());
    }

    @Operation(
            summary = "Delete a permission",
            description = "Elimina un permiso por id. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Not content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
