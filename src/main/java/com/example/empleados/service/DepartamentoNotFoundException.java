package com.example.empleados.service;

public class DepartamentoNotFoundException extends RuntimeException {

    public DepartamentoNotFoundException(Long id) {
        super("No existe departamento con id: " + id);
    }
}
