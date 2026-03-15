package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void validateUpdate_shouldPassWhenEmployeeFieldsAreNullAndOnlyAccessFieldsAreSent() {
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            null,
            null,
            null,
            "admin@empresa.com",
            "admin123"
        );

        assertDoesNotThrow(() -> validationService.validateUpdate(request));
    }

    @Test
    void validateUpdate_shouldFailWhenProvidedFieldIsBlank() {
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            "   ",
            null,
            null,
            null,
            null
        );

        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateUpdate(request));
        assertEquals("nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    void validateUpdate_shouldFailWhenPasswordIsSentWithoutEmail() {
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            null,
            "admin123"
        );

        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateUpdate(request));
        assertEquals("si envías password también debes enviar email", exception.getMessage());
    }

    @Test
    void validateUpdate_shouldPassWhenEmailAndPasswordAreSentTogether() {
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            "admin@empresa.com",
            "admin123"
        );

        assertDoesNotThrow(() -> validationService.validateUpdate(request));
    }

    @Test
    void validateCreate_shouldFailWhenOnlyEmailIsSent() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            null,
            "admin@empresa.com",
            null
        );

        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateCreate(request));
        assertEquals("email y password deben enviarse juntos", exception.getMessage());
    }

    @Test
    void validateCreate_shouldPassWhenEmailAndPasswordAreBothSent() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            null,
            "admin@empresa.com",
            "admin123"
        );

        assertDoesNotThrow(() -> validationService.validateCreate(request));
    }
}
