package com.example.empleados.service;

public class DepartamentoInUseException extends RuntimeException {

    public DepartamentoInUseException(Long id) {
        super("No se puede eliminar el departamento " + id + " porque tiene empleados asociados");
    }
}
