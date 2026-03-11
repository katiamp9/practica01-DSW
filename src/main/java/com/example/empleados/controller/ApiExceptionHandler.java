package com.example.empleados.controller;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.service.ClaveCollisionException;
import com.example.empleados.service.EmpleadoNotFoundException;
import com.example.empleados.service.InvalidPaginationException;
import com.example.empleados.service.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EmpleadoNotFoundException.class)
    public ResponseEntity<EmpleadoDtos.ErrorResponse> handleNotFound(EmpleadoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new EmpleadoDtos.ErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<EmpleadoDtos.ErrorResponse> handleValidation(Exception ex) {
        String message = ex instanceof MethodArgumentNotValidException manv
            ? manv.getBindingResult().getAllErrors().stream().findFirst().map(err -> err.getDefaultMessage()).orElse("Datos inválidos")
            : ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new EmpleadoDtos.ErrorResponse("BAD_REQUEST", message));
    }

    @ExceptionHandler(ClaveCollisionException.class)
    public ResponseEntity<EmpleadoDtos.ErrorResponse> handleCollision(ClaveCollisionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new EmpleadoDtos.ErrorResponse("CONFLICT", ex.getMessage()));
    }

    @ExceptionHandler({InvalidPaginationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<EmpleadoDtos.ErrorResponse> handleInvalidPagination(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(new EmpleadoDtos.ErrorResponse("UNPROCESSABLE_ENTITY", ex.getMessage()));
    }
}
