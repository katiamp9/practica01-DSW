package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoUpdateService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoValidationService validationService;

    public EmpleadoUpdateService(EmpleadoRepository empleadoRepository, EmpleadoValidationService validationService) {
        this.empleadoRepository = empleadoRepository;
        this.validationService = validationService;
    }

    public Empleado update(String clave, EmpleadoDtos.EmpleadoUpdateRequest request) {
        validationService.validateUpdate(request);

        Empleado empleado = empleadoRepository.findById(clave)
            .orElseThrow(() -> new EmpleadoNotFoundException(clave));

        empleado.setNombre(request.nombre().trim());
        empleado.setDireccion(request.direccion().trim());
        empleado.setTelefono(request.telefono().trim());

        return empleadoRepository.save(empleado);
    }
}
