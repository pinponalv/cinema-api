package com.example.cinema_api.controller;

import com.example.cinema_api.dto.*;
import com.example.cinema_api.entity.RefreshToken;
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
@Tag(name = "Auth", description = "Authentication API Operations")
@RequiredArgsConstructor
public class AuthController {
    private final CustomUserDetailsService userDetailsService;
    private final IUserService userService;

    @Operation(
            summary = "Login request",
            description = "Autentica un usuario con username/email y password, y retorna un token JWT. Endpoint público."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(userDetailsService.loginUser(authLoginRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Register request",
            description = "Registra un nuevo usuario público con el rol USER por defecto. Endpoint público, no requiere autenticación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "409", description = "Resource already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest userRequest){
        UserResponse registerResponse = userService.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }


    @Operation(
            summary = "Refresh access token",
            description = "Genera un nuevo access token a partir de un refresh token válido. Endpoint público."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok request"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return new ResponseEntity<>(userDetailsService.refreshToken(refreshTokenRequest.getRefreshToken()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        userDetailsService.revokeRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

}
