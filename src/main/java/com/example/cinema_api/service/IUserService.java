package com.example.cinema_api.service;

import com.example.cinema_api.dto.UserRequest;
import com.example.cinema_api.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserResponse registerUser(UserRequest userRequest);
    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(Long id,UserRequest userRequest);
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(Long id);
    void deleteUser(Long id);
    String encriptPassword(String password);
}
