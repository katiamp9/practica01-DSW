package com.example.empleados.service;

public class EmpleadoNotFoundException extends RuntimeException {

    public EmpleadoNotFoundException(String clave) {
        super("No existe empleado con clave: " + clave);
    }
}
