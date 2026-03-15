package com.example.empleados.controller;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;

final class EmpleadoMapper {

    private EmpleadoMapper() {
    }

    static EmpleadoDtos.EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoDtos.EmpleadoResponse(
            empleado.getClave(),
            empleado.getNombre(),
            empleado.getDireccion(),
            empleado.getTelefono()
        );
    }
}
