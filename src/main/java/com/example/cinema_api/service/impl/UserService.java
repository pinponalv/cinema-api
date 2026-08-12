package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.*;
import com.example.cinema_api.entity.Roles;
import com.example.cinema_api.entity.UserSec;
import com.example.cinema_api.exception.DuplicateResourceException;
import com.example.cinema_api.exception.InvalidRequestException;
import com.example.cinema_api.exception.ResourceNotFoundException;
import com.example.cinema_api.mapper.UserMapper;
import com.example.cinema_api.repository.RolesRepository;
import com.example.cinema_api.repository.UserRepository;
import com.example.cinema_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; //inyecto passwordEncoder de spring security

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        validateUser(userRequest);

        //busco el rol por default que le quiero pasar
        Roles role = rolesRepository.findById(2L).orElseThrow(() -> new ResourceNotFoundException("rol not found"));

        UserSec user = new UserSec();
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setPassword(encriptPassword(userRequest.getPassword()));
        user.getRoles().add(role);

        UserSec savedUser = userRepository.save(user);

        return userMapper.toUserDTO(savedUser);
    }
    
    @Override
    public UserResponse createUser(UserRequest userRequest) {
        validateUser(userRequest);

        if(userRequest.getRoles() == null || userRequest.getRoles().isEmpty()){
            throw new InvalidRequestException("specify a role");
        }

        Set<Roles> roles = resolveRoles(userRequest.getRoles());

        UserSec user  = new UserSec();
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setPassword(encriptPassword(userRequest.getPassword()));
        user.setRoles(roles);

        UserSec savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UserPatchRequest userRequest) {
        UserSec user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(userRequest.getEmail() != null){
            user.setEmail(userRequest.getEmail());
        }
        if(userRequest.getUsername() != null){
            user.setUsername(userRequest.getUsername());
        }
        if(userRequest.getPassword() != null){
            user.setPassword(encriptPassword(userRequest.getPassword()));
        }
        if(userRequest.getRoles() != null){
            user.setRoles(resolveRoles(userRequest.getRoles()));
        }

        UserSec savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<UserSec> users = userRepository.findAll(pageable);
        List<UserResponse> userResponses = new ArrayList<>();

        for(UserSec user : users.getContent()){
            UserResponse userResponse = userMapper.toUserDTO(user);
            userResponses.add(userResponse);
        }
        return new PageImpl<>(userResponses, pageable, users.getTotalElements());
    }

    @Override
    public UserResponse getUserById(Long id) {
        UserSec user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public String encriptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    //Me permite reutilizar este codigo sin tener que escribir cada proceso de nuevo
    private Set<Roles> resolveRoles(Set<RoleIdRequest> rolesIdRequest){
        Set<Roles> roles = new HashSet<>();
        for(RoleIdRequest role: rolesIdRequest){
            Roles readRole = rolesRepository.findById(role.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role Not found: " + role.getId()));
            roles.add(readRole);
        }
        return roles;
    }

    private void validateUser(UserRequest userRequest){
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if(userRepository.existsByUsername(userRequest.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
    }
}
