package com.example.empleados.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

        @NotNull(message = "departamentoId es obligatorio")
        Long departamentoId,

        String clave,

        String email,

        String password
    ) {
        public EmpleadoCreateRequest(String nombre, String direccion, String telefono, String clave) {
            this(nombre, direccion, telefono, null, clave, null, null);
        }

        public EmpleadoCreateRequest(
            String nombre,
            String direccion,
            String telefono,
            Long departamentoId,
            String clave
        ) {
            this(nombre, direccion, telefono, departamentoId, clave, null, null);
        }
    }

    public record EmpleadoUpdateRequest(
        @Size(max = 100, message = "nombre debe tener máximo 100 caracteres")
        String nombre,

        @Size(max = 100, message = "direccion debe tener máximo 100 caracteres")
        String direccion,

        @Size(max = 100, message = "telefono debe tener máximo 100 caracteres")
        String telefono,

        Long departamentoId,

        String email,

        String password
    ) {
        public EmpleadoUpdateRequest(String nombre, String direccion, String telefono) {
            this(nombre, direccion, telefono, null, null, null);
        }

        public EmpleadoUpdateRequest(
            String nombre,
            String direccion,
            String telefono,
            String email,
            String password
        ) {
            this(nombre, direccion, telefono, null, email, password);
        }
    }

    public record EmpleadoResponse(
        String clave,
        String nombre,
        String direccion,
        String telefono,
        Long departamentoId,
        String rol
    ) {
    }

    public record ErrorResponse(
        String code,
        String message
    ) {
    }
}
