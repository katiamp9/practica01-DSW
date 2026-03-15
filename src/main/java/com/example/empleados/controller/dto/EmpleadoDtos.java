package com.example.empleados.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class EmpleadoDtos {

    private EmpleadoDtos() {
    }

    public record EmpleadoCreateRequest(
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 100, message = "nombre debe tener máximo 100 caracteres")
        String nombre,

        @NotBlank(message = "direccion es obligatoria")
        @Size(max = 100, message = "direccion debe tener máximo 100 caracteres")
        String direccion,

        @NotBlank(message = "telefono es obligatorio")
        @Size(max = 100, message = "telefono debe tener máximo 100 caracteres")
        String telefono,

        String clave,

        String email,

        String password
    ) {
        public EmpleadoCreateRequest(String nombre, String direccion, String telefono, String clave) {
            this(nombre, direccion, telefono, clave, null, null);
        }
    }

    public record EmpleadoUpdateRequest(
        @Size(max = 100, message = "nombre debe tener máximo 100 caracteres")
        String nombre,

        @Size(max = 100, message = "direccion debe tener máximo 100 caracteres")
        String direccion,

        @Size(max = 100, message = "telefono debe tener máximo 100 caracteres")
        String telefono,

        String email,

        String password
    ) {
        public EmpleadoUpdateRequest(String nombre, String direccion, String telefono) {
            this(nombre, direccion, telefono, null, null);
        }
    }

    public record EmpleadoResponse(
        String clave,
        String nombre,
        String direccion,
        String telefono
    ) {
    }

    public record ErrorResponse(
        String code,
        String message
    ) {
    }
}
