package com.example.empleados.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(
        @NotBlank(message = "email es obligatorio")
        @Email(message = "email debe tener un formato válido")
        String email,

        @NotBlank(message = "password es obligatorio")
        String password
    ) {
    }

    public record LoginSuccessResponse(
        boolean authenticated,
        String rol,
        String nombre
    ) {
    }

    public record ErrorResponse(
        String code,
        String message
    ) {
    }
}