package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoUpdateService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoValidationService validationService;
    private final CredencialEmpleadoService credencialEmpleadoService;

    public EmpleadoUpdateService(
        EmpleadoRepository empleadoRepository,
        EmpleadoValidationService validationService,
        CredencialEmpleadoService credencialEmpleadoService
    ) {
        this.empleadoRepository = empleadoRepository;
        this.validationService = validationService;
        this.credencialEmpleadoService = credencialEmpleadoService;
    }

    @Transactional
    public Empleado update(String clave, EmpleadoDtos.EmpleadoUpdateRequest request) {
        validationService.validateUpdate(request);

        Empleado empleado = empleadoRepository.findById(clave)
            .orElseThrow(() -> new EmpleadoNotFoundException(clave));

        if (request.nombre() != null) {
            empleado.setNombre(request.nombre().trim());
        }
        if (request.direccion() != null) {
            empleado.setDireccion(request.direccion().trim());
        }
        if (request.telefono() != null) {
            empleado.setTelefono(request.telefono().trim());
        }

        if (request.email() != null && !request.email().trim().isEmpty()) {
            credencialEmpleadoService.upsertAccessForEmpleado(
                clave,
                request.email(),
                request.password()
            );
        }

        return empleadoRepository.save(empleado);
    }
}
