package com.example.cinema_api.controller;

import com.example.cinema_api.dto.UserRequest;
import com.example.cinema_api.dto.UserResponse;
import com.example.cinema_api.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Users API Operations")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest){
        UserResponse responseUserDTO = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUserDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/user/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest){
        UserResponse responseUserDTO = userService.updateUser(id, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseUserDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers(){
        List<UserResponse> userList = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id){
        UserResponse responseUserDTO = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseUserDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUserById(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
