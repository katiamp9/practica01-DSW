package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmpleadoValidationServiceTest {

    private final EmpleadoValidationService validationService = new EmpleadoValidationService();

    @Test
    void validateCreate_shouldFailWhenClaveSentByClient() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            "EMP-9999"
        );

        assertThrows(ValidationException.class, () -> validationService.validateCreate(request));
    }

    @Test
    void validateCreate_shouldFailWhenNameTooLong() {
        String tooLong = "a".repeat(101);
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            tooLong,
            "Calle 1",
            "555-0101",
            null
        );

        assertThrows(ValidationException.class, () -> validationService.validateCreate(request));
    }

    @Test
    void validateUpdate_shouldPassWithValidData() {
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            "Ana",
            "Calle 1",
            "555-0101"
        );

        assertDoesNotThrow(() -> validationService.validateUpdate(request));
    }
}
