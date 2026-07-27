package com.example.cinema_api.controller;

import com.example.cinema_api.dto.AuthLoginRequest;
import com.example.cinema_api.dto.AuthResponse;
import com.example.cinema_api.security.service.CustomUserDetailsService;
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(userDetailsService.loginUser(authLoginRequest), HttpStatus.OK);
    }
}
