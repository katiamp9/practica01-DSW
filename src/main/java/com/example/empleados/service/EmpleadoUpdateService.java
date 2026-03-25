package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.CuentaEmpleadoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoUpdateService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoValidationService validationService;
    private final DepartamentoValidationService departamentoValidationService;
    private final CuentaEmpleadoRepository cuentaEmpleadoRepository;
    private final CredencialEmpleadoService credencialEmpleadoService;

    public EmpleadoUpdateService(
        EmpleadoRepository empleadoRepository,
        EmpleadoValidationService validationService,
        DepartamentoValidationService departamentoValidationService,
        CuentaEmpleadoRepository cuentaEmpleadoRepository,
        CredencialEmpleadoService credencialEmpleadoService
    ) {
        this.empleadoRepository = empleadoRepository;
        this.validationService = validationService;
        this.departamentoValidationService = departamentoValidationService;
        this.cuentaEmpleadoRepository = cuentaEmpleadoRepository;
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
        if (request.departamentoId() != null) {
            empleado.setDepartamento(departamentoValidationService.requireDepartamento(request.departamentoId()));
        }

        empleado.setRol(Roles.normalizeOrDefault(empleado.getRol()));

        assertImmutableEmail(clave, request.email());

        boolean hasEmail = request.email() != null && !request.email().trim().isEmpty();
        boolean hasPassword = request.password() != null && !request.password().trim().isEmpty();
        if (hasEmail || hasPassword) {
            credencialEmpleadoService.upsertAccessForEmpleado(
                clave,
                request.email(),
                request.password()
            );
        }

        return empleadoRepository.save(empleado);
    }

    private void assertImmutableEmail(String clave, String requestedEmail) {
        if (requestedEmail == null || requestedEmail.trim().isEmpty()) {
            return;
        }

        String normalizedRequested = requestedEmail.trim().toLowerCase(Locale.ROOT);
        String persistedEmail = cuentaEmpleadoRepository.findByEmpleadoClave(clave)
            .map(cuentaEmpleado -> cuentaEmpleado.getCorreo() == null ? "" : cuentaEmpleado.getCorreo().trim().toLowerCase(Locale.ROOT))
            .orElse("");

        if (persistedEmail.isEmpty()) {
            throw new ValidationException("email es inmutable en edición de empleado");
        }

        if (!normalizedRequested.equals(persistedEmail)) {
            throw new ValidationException("email es inmutable en edición de empleado");
        }
    }
}
