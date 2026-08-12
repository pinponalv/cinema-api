package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.PermissionIdRequest;
import com.example.cinema_api.dto.RolePatchRequest;
import com.example.cinema_api.dto.RoleRequest;
import com.example.cinema_api.dto.RoleResponse;
import com.example.cinema_api.entity.Permission;
import com.example.cinema_api.entity.Roles;
import com.example.cinema_api.exception.ResourceNotFoundException;
import com.example.cinema_api.repository.PermissionRepository;
import com.example.cinema_api.repository.RolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RolesRepository rolesRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @InjectMocks
    private RoleService roleService;

    private Roles roles;
    private Permission permission;
    private RoleRequest roleRequest;

    @BeforeEach
    void setUp(){
        permission = new Permission(1L, "CREATE");
        roles = new Roles(1L,"ADMIN" ,Set.of(permission));
        roleRequest = new RoleRequest("ADMIN", Set.of(new PermissionIdRequest(1L)));
    }

    @Test
    void create(){

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(rolesRepository.save(any(Roles.class))).thenReturn(roles);

        RoleResponse roleResponse = roleService.createRole(roleRequest);

        //afirma que el response no es nulo
        assertThat(roleResponse).isNotNull();
        assertThat(roleResponse.getId()).isEqualTo(1L);
        assertThat(roleResponse.getRole()).isEqualTo("ADMIN");
        assertThat(roleResponse.getPermissions()).hasSize(1);

        //verifica que almenos una vez el repositorio llame a find by id
        verify(permissionRepository, times(1)).findById(1L);
        verify(rolesRepository, times(1)).save(any(Roles.class));
    }

    @Test
    void updateRole(){
        RolePatchRequest updateRequest = new RolePatchRequest("SUPER_ADMIN", null);
        when(rolesRepository.findById(1L)).thenReturn(Optional.of(roles));
        when(rolesRepository.save(any(Roles.class))).thenReturn(roles);

        RoleResponse roleResponse = roleService.updateRole(1L, updateRequest);

        assertThat(roleResponse.getRole()).isEqualTo("SUPER_ADMIN");
        assertThat(roleResponse.getPermissions()).hasSize(1);
        verify(rolesRepository).save(roles);
        //es never porque en el service es if (role != null)
        //si es nulo no actualiza este dato
        verify(permissionRepository, never()).findById(any());
    }

    @Test
    void updateRole_throwExceptionWhenRoleNotFound(){
        when(rolesRepository.findById(99L)).thenReturn(Optional.empty());

        RolePatchRequest patchRequest = new RolePatchRequest("ADMIN", Set.of(new PermissionIdRequest(1L)));
        assertThatThrownBy(() -> roleService.updateRole(99L, patchRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Role not found");

        verify(rolesRepository, never()).save(any());
    }

    @Test
    void findAllRoles(){
        when(rolesRepository.findAll()).thenReturn(List.of(roles));

        List<RoleResponse> result = roleService.findAllRoles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRole()).isEqualTo("ADMIN");
        verify(rolesRepository, times(1)).findAll();
    }

    @Test
    void findRoleById(){
        when(rolesRepository.findById(1L)).thenReturn(Optional.of(roles));

        RoleResponse roleResponse = roleService.findRoleById(1L);

        assertThat(roleResponse.getId()).isEqualTo(1L);
        assertThat(roleResponse.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void findRoleById_throwExceptionWhenRoleNotFound(){
        when(rolesRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.findRoleById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Role not found");
    }

    @Test
    void deleteRole(){
        when(rolesRepository.existsById(1L)).thenReturn(true);

        roleService.deleteRole(1L);

        verify(rolesRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRole_throwExceptionWhenRoleNotFound(){
        when(rolesRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> roleService.deleteRole(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Role not found");

        verify(rolesRepository, never()).deleteById(any());
    }
}
