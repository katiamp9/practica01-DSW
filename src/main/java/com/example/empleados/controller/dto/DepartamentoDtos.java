package com.example.empleados.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class DepartamentoDtos {

    private DepartamentoDtos() {
    }

    public record DepartamentoRequest(
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 100, message = "nombre debe tener máximo 100 caracteres")
        String nombre
    ) {
    }

    public record DepartamentoResponse(
        Long id,
        String nombre
    ) {
    }

    public record DepartamentoListResponse(
        Long id,
        String nombre,
        Long totalEmpleados
    ) {
    }
}
