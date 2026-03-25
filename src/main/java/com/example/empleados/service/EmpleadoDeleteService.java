package com.example.empleados.service;

import com.example.empleados.repository.CuentaEmpleadoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoDeleteService {

    private static final String MAIN_ADMIN_EMAIL = "admin@empresa.com";

    private final EmpleadoRepository empleadoRepository;
    private final CuentaEmpleadoRepository cuentaEmpleadoRepository;

    public EmpleadoDeleteService(EmpleadoRepository empleadoRepository, CuentaEmpleadoRepository cuentaEmpleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        this.cuentaEmpleadoRepository = cuentaEmpleadoRepository;
    }

    @Transactional
    public void delete(String clave) {
        if (!empleadoRepository.existsById(clave)) {
            throw new EmpleadoNotFoundException(clave);
        }

        boolean isMainAdmin = cuentaEmpleadoRepository.findByEmpleadoClave(clave)
            .map(cuenta -> MAIN_ADMIN_EMAIL.equalsIgnoreCase(cuenta.getCorreo()))
            .orElse(false);

        if (isMainAdmin) {
            throw new ForbiddenOperationException("No se permite eliminar la cuenta del admin principal");
        }

        try {
            cuentaEmpleadoRepository.findByEmpleadoClave(clave)
                .ifPresent(cuentaEmpleado -> {
                    cuentaEmpleadoRepository.deleteById(cuentaEmpleado.getId());
                    cuentaEmpleadoRepository.flush();
                });
            empleadoRepository.deleteById(clave);
        } catch (DataIntegrityViolationException ex) {
            throw new ValidationException("No se puede eliminar el empleado porque tiene dependencias asociadas");
        }
    }
}
