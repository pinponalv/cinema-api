package com.example.cinema_api.service;

import com.example.cinema_api.dto.UserRequest;
import com.example.cinema_api.dto.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse registerUser(UserRequest userRequest);
    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(Long id,UserRequest userRequest);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    void deleteUser(Long id);
    String encriptPassword(String password);
}
