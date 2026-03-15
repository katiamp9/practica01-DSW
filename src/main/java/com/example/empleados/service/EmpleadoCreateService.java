package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoCreateService {

    private static final int MAX_RETRIES = 3;

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoValidationService validationService;
    private final ClaveEmpleadoGenerator claveGenerator;
    private final CredencialEmpleadoService credencialEmpleadoService;

    public EmpleadoCreateService(
        EmpleadoRepository empleadoRepository,
        EmpleadoValidationService validationService,
        ClaveEmpleadoGenerator claveGenerator,
        CredencialEmpleadoService credencialEmpleadoService
    ) {
        this.empleadoRepository = empleadoRepository;
        this.validationService = validationService;
        this.claveGenerator = claveGenerator;
        this.credencialEmpleadoService = credencialEmpleadoService;
    }

    @Transactional
    public Empleado create(EmpleadoDtos.EmpleadoCreateRequest request) {
        validationService.validateCreate(request);

        String lastGeneratedKey = null;
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            ClaveEmpleadoGenerator.ClaveGenerada claveGenerada = claveGenerator.generate();
            lastGeneratedKey = claveGenerada.clave();

            Empleado empleado = new Empleado();
            empleado.setClave(claveGenerada.clave());
            empleado.setPrefijo(claveGenerada.prefijo());
            empleado.setConsecutivo(claveGenerada.consecutivo());
            empleado.setNombre(request.nombre().trim());
            empleado.setDireccion(request.direccion().trim());
            empleado.setTelefono(request.telefono().trim());

            try {
                Empleado savedEmpleado = empleadoRepository.save(empleado);
                createOptionalAccess(savedEmpleado, request);
                return savedEmpleado;
            } catch (DataIntegrityViolationException ex) {
                if (attempt == MAX_RETRIES) {
                    throw new ClaveCollisionException(lastGeneratedKey);
                }
            }
        }

        throw new ClaveCollisionException(lastGeneratedKey == null ? "UNKNOWN" : lastGeneratedKey);
    }

    private void createOptionalAccess(Empleado empleado, EmpleadoDtos.EmpleadoCreateRequest request) {
        boolean hasEmail = request.email() != null && !request.email().trim().isEmpty();
        boolean hasPassword = request.password() != null && !request.password().trim().isEmpty();

        if (!hasEmail && !hasPassword) {
            return;
        }

        credencialEmpleadoService.createAccountWithCredential(
            request.email(),
            empleado.getClave(),
            request.password()
        );
    }
}
