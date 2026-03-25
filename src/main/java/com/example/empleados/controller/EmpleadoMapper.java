package com.example.empleados.controller;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoListProjection;

final class EmpleadoMapper {

    private EmpleadoMapper() {
    }

    static EmpleadoDtos.EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoDtos.EmpleadoResponse(
            empleado.getClave(),
            empleado.getNombre(),
            null,
            empleado.getDireccion(),
            empleado.getTelefono(),
            empleado.getDepartamento() == null ? null : empleado.getDepartamento().getId(),
            empleado.getRol()
        );
    }

    static EmpleadoDtos.EmpleadoResponse toResponse(EmpleadoListProjection empleado) {
        return new EmpleadoDtos.EmpleadoResponse(
            empleado.getClave(),
            empleado.getNombre(),
            empleado.getEmail(),
            empleado.getDireccion(),
            empleado.getTelefono(),
            empleado.getDepartamentoId(),
            empleado.getRol()
        );
    }
}
