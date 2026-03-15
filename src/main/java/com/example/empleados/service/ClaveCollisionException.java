package com.example.empleados.service;

public class ClaveCollisionException extends RuntimeException {

    public ClaveCollisionException(String clave) {
        super("Colisión de clave generada: " + clave);
    }
}
