package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.PermissionRequest;
import com.example.cinema_api.dto.PermissionResponse;
import com.example.cinema_api.entity.Permission;
import com.example.cinema_api.exception.ResourceNotFoundException;
import com.example.cinema_api.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;
    @InjectMocks
    private PermissionService permissionService;

    private Permission permission;
    private PermissionRequest permissionRequest;

    //que se ejecute antes de los test
    @BeforeEach
    void setUp(){
        permission = new Permission(1L, "CREATE");
        permissionRequest = new PermissionRequest("CREATE");
    }

    @Test
    void createPermission(){
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        PermissionResponse response = permissionService.createPermission(permissionRequest);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getPermission()).isEqualTo("CREATE");
        verify(permissionRepository, times(1)).save(any(Permission.class));
    }

    @Test
    void updatePermission(){
        PermissionRequest request = new PermissionRequest("DELETE");
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);
        PermissionResponse response = permissionService.updatePermission(1L, request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getPermission()).isEqualTo("DELETE");
        verify(permissionRepository).save(permission);
    }

    @Test
    void updatePermission_throwExceptionWhenPermissionNotFound(){
        when(permissionRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> permissionService.updatePermission(99L, permissionRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Permission not found");

        verify(permissionRepository, never()).save(any());
    }

    @Test
    void findAllPermission(){
        List<Permission> permissionList = List.of(permission);
        when(permissionRepository.findAll()).thenReturn(permissionList);
        var result = permissionService.findAllPermissions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getPermission()).isEqualTo("CREATE");
        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    void deletePermission(){
        when(permissionRepository.existsById(1L)).thenReturn(true);
        permissionService.deletePermission(1L);
        verify(permissionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePermission_throwExceptionWhenPermissionNotfound(){
        when(permissionRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> permissionService.deletePermission(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Permission not found");

        verify(permissionRepository, never()).deleteById(any());
    }

}
