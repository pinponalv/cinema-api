package com.example.cinema_api.controller;

import com.example.cinema_api.dto.AuthLoginRequest;
import com.example.cinema_api.dto.AuthResponse;
import com.example.cinema_api.dto.UserRequest;
import com.example.cinema_api.dto.UserResponse;
import com.example.cinema_api.security.service.CustomUserDetailsService;
import com.example.cinema_api.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "")
@RequiredArgsConstructor
public class AuthController {
    private final CustomUserDetailsService userDetailsService;
    private final IUserService userService;

    @Operation(summary = "Login request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthiorized"),
            @ApiResponse(responseCode = "403", description = "Forbiddden")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(userDetailsService.loginUser(authLoginRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest userRequest){
        UserResponse registerResponse = userService.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

}
