package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.RoleIdRequest;
import com.example.cinema_api.dto.RoleResponse;
import com.example.cinema_api.dto.UserRequest;
import com.example.cinema_api.dto.UserResponse;
import com.example.cinema_api.entity.Roles;
import com.example.cinema_api.entity.UserSec;
import com.example.cinema_api.exception.DuplicateResourceException;
import com.example.cinema_api.exception.InvalidRequestException;
import com.example.cinema_api.exception.ResourceNotFoundException;
import com.example.cinema_api.mapper.UserMapper;
import com.example.cinema_api.repository.RolesRepository;
import com.example.cinema_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RolesRepository rolesRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Roles defaultRole;
    private Roles adminRole;
    private UserSec userSec;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        defaultRole = new Roles(2L, "USER", Set.of());
        adminRole = new Roles(1L, "ADMIN", Set.of());

        userSec = new UserSec();
        userSec.setId(1L);
        userSec.setEmail("juan@gmail.com");
        userSec.setUsername("juanito123");
        userSec.setPassword("encodedPassword");

        userRequest = new UserRequest("juan@gmail.com", "juanito123", "12345678", Set.of(new RoleIdRequest(1L)));

        userResponse = new UserResponse(1L, "juan@gmail.com", "juanito123", Set.of(new RoleResponse(1L, "ADMIN", Set.of())));
    }

    // ---------- registerUser ----------

    @Test
    void registerUser() {
        //simulmos que returna false porque el email no existe
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        //aqui le seteamos el rol user
        when(rolesRepository.findById(2L)).thenReturn(Optional.of(defaultRole));

        //simulamos la encriptacion retornando encodedPassword
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        //cuando guarde algun objeto user voy a retornar userSec
        when(userRepository.save(any(UserSec.class))).thenReturn(userSec);
        when(userMapper.toUserDTO(userSec)).thenReturn(userResponse);

        UserResponse response = userService.registerUser(userRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("juan@gmail.com");
        verify(rolesRepository, times(1)).findById(2L);
        verify(userRepository, times(1)).save(any(UserSec.class));
    }

    @Test
    void registerUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(userRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_shouldThrowExceptionWhenUsernameAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(userRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Username already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_shouldThrowExceptionWhenDefaultRoleNotFound() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        //cuando el rol no se encuentre
        when(rolesRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.registerUser(userRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("rol not found");

        verify(userRepository, never()).save(any());
    }

    // ---------- createUser ----------

    @Test
    void createUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(rolesRepository.findById(1L)).thenReturn(Optional.of(adminRole));
        //simulamos que encriptamos lapassword con el mensaje encodedpassword
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserSec.class))).thenReturn(userSec);
        when(userMapper.toUserDTO(userSec)).thenReturn(userResponse);

        UserResponse response = userService.createUser(userRequest);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("juanito123");
        verify(rolesRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(UserSec.class));
    }

    @Test
    void createUser_shouldThrowExceptionWhenRolesAreEmpty() {
        UserRequest requestWithoutRoles = new UserRequest("juan@gmail.com", "juanito123", "12345678", Set.of());
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(requestWithoutRoles))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("specify a role");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowExceptionWhenRoleNotFound() {
        UserRequest requestWithMissingRole = new UserRequest("juan@gmail.com", "juanito123", "12345678", Set.of(new RoleIdRequest(99L)));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(rolesRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.createUser(requestWithMissingRole))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Role Not found: 99");

        verify(userRepository, never()).save(any());
    }

    // ---------- updateUser ----------

    @Test
    void updateUser_shouldUpdateOnlyProvidedFields() {
        UserRequest updateRequest = new UserRequest("nuevo@gmail.com", null, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userSec));
        when(userRepository.save(any(UserSec.class))).thenReturn(userSec);
        when(userMapper.toUserDTO(userSec)).thenReturn(userResponse);

        UserResponse response = userService.updateUser(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(userRepository).save(userSec);
        verify(passwordEncoder, never()).encode(any());
        verify(rolesRepository, never()).findById(any());
    }

    @Test
    void updateUser_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(99L, userRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).save(any());
    }

    // ---------- getAllUsers ----------

    @Test
    void getAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<UserSec> usersPage = new PageImpl<>(List.of(userSec), pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(usersPage);
        when(userMapper.toUserDTO(userSec)).thenReturn(userResponse);

        var result = userService.getAllUsers(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("juan@gmail.com");
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    // ---------- getUserById ----------

    @Test
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userSec));
        when(userMapper.toUserDTO(userSec)).thenReturn(userResponse);

        UserResponse response = userService.getUserById(1L);

        assertThat(response.getEmail()).isEqualTo("juan@gmail.com");
    }

    @Test
    void getUserById_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    // ---------- deleteUser ----------

    @Test
    void deleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).deleteById(any());
    }
}
