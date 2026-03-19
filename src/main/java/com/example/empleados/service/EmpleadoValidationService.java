package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoValidationService {

    public void validateCreate(EmpleadoDtos.EmpleadoCreateRequest request) {
        if (request.clave() != null && !request.clave().isBlank()) {
            throw new ValidationException("No se permite enviar clave manualmente");
        }
        if (request.departamentoId() == null) {
            throw new ValidationException("departamentoId es obligatorio");
        }
        validateCommon(request.nombre(), request.direccion(), request.telefono());
        validateOptionalAccess(request.email(), request.password());
    }

    public void validateUpdate(EmpleadoDtos.EmpleadoUpdateRequest request) {
        validateTextIfPresent("nombre", request.nombre());
        validateTextIfPresent("direccion", request.direccion());
        validateTextIfPresent("telefono", request.telefono());
        validateDepartamentoIfPresent(request.departamentoId());
        validateOptionalAccessForUpdate(request.email(), request.password());
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

    private void validateOptionalAccess(String email, String password) {
        boolean hasEmail = email != null && !email.trim().isEmpty();
        boolean hasPassword = password != null && !password.trim().isEmpty();

        if (hasEmail != hasPassword) {
            throw new ValidationException("email y password deben enviarse juntos");
        }

        if (!hasEmail) {
            return;
        }

        if (!email.contains("@") || email.length() > 254) {
            throw new ValidationException("email debe tener un formato válido");
        }

        if (password.length() > 200) {
            throw new ValidationException("password debe tener máximo 200 caracteres");
        }
    }

    private void validateOptionalAccessForUpdate(String email, String password) {
        boolean hasEmail = email != null && !email.trim().isEmpty();
        boolean hasPassword = password != null && !password.trim().isEmpty();

        if (hasPassword && !hasEmail) {
            throw new ValidationException("si envías password también debes enviar email");
        }

        if (!hasEmail) {
            return;
        }

        if (!email.contains("@") || email.length() > 254) {
            throw new ValidationException("email debe tener un formato válido");
        }

        if (hasPassword && password.length() > 200) {
            throw new ValidationException("password debe tener máximo 200 caracteres");
        }
    }

    private void validateTextIfPresent(String field, String value) {
        if (value == null) {
            return;
        }
        if (value.trim().isEmpty()) {
            throw new ValidationException(field + " no puede estar vacío");
        }
        if (value.length() > 100) {
            throw new ValidationException(field + " debe tener máximo 100 caracteres");
        }
    }

    private void validateDepartamentoIfPresent(Long departamentoId) {
        if (departamentoId != null && departamentoId < 1) {
            throw new ValidationException("departamentoId debe ser mayor a 0");
        }
    }
}
