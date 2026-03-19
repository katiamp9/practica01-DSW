package com.example.empleados.controller;

import com.example.empleados.controller.dto.AuthDtos;
import com.example.empleados.service.AuthLoginService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthLoginService authLoginService;

    public AuthController(AuthLoginService authLoginService) {
        this.authLoginService = authLoginService;
    }

    @PostMapping("/login")
    public AuthDtos.LoginSuccessResponse login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        return authLoginService.login(request);
    }
}