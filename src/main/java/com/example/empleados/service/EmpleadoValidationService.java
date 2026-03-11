package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoValidationService {

    public void validateCreate(EmpleadoDtos.EmpleadoCreateRequest request) {
        if (request.clave() != null && !request.clave().isBlank()) {
            throw new ValidationException("No se permite enviar clave manualmente");
        }
        validateCommon(request.nombre(), request.direccion(), request.telefono());
    }

    public void validateUpdate(EmpleadoDtos.EmpleadoUpdateRequest request) {
        validateCommon(request.nombre(), request.direccion(), request.telefono());
    }

    private void validateCommon(String nombre, String direccion, String telefono) {
        validateText("nombre", nombre);
        validateText("direccion", direccion);
        validateText("telefono", telefono);
    }

    private void validateText(String field, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(field + " es obligatorio");
        }
        if (value.length() > 100) {
            throw new ValidationException(field + " debe tener máximo 100 caracteres");
        }
    }
}
