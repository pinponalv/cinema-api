package com.example.cinema_api.service.impl;

import com.example.cinema_api.dto.*;
import com.example.cinema_api.entity.Roles;
import com.example.cinema_api.entity.UserSec;
import com.example.cinema_api.mapper.UserMapper;
import com.example.cinema_api.repository.RolesRepository;
import com.example.cinema_api.repository.UserRepository;
import com.example.cinema_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    //TODO: Inyectar passwordEncoder de spring security
    //private final PasswordEncoder passwordEncoder;

    //TODO: Una opcion que tengo al crear un usuario es que al crear automaticamente setearle el rol como USER
    @Override
    public UserResponse createUser(UserRequest userRequest) {
        Set<Roles> roles = new HashSet<>();
        Roles readRole;

        //get permisos
        for (RoleIdRequest role : userRequest.getRoles()){
            readRole = rolesRepository.findById(role.getId())
                    .orElseThrow(() -> new RuntimeException("Role Not found"));
            roles.add(readRole);
        }

        UserSec user  = new UserSec();
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setPassword(this.encriptPassword(userRequest.getPassword()));
        user.setRoles(roles);
        UserSec savedUser = userRepository.save(user);

        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        UserSec user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if(userRequest.getEmail() != null){
            user.setEmail(userRequest.getEmail());
        }
        if(userRequest.getUsername() != null){
            user.setUsername(userRequest.getUsername());
        }
        if(userRequest.getPassword() != null){
            user.setPassword(userRequest.getPassword());
        }

        UserSec savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserSec> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();

        for(UserSec user : users){
            UserResponse userResponse = userMapper.toUserDTO(user);
            userResponses.add(userResponse);
        }

        return userResponses;
    }

    @Override
    public UserResponse getUserById(Long id) {
        UserSec user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        UserSec user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(user.getId());
    }

    @Override
    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
