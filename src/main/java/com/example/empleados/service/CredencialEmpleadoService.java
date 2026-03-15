package com.example.empleados.service;

import com.example.empleados.domain.CredencialEmpleado;
import com.example.empleados.domain.CuentaEmpleado;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.CredencialEmpleadoRepository;
import com.example.empleados.repository.CuentaEmpleadoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import java.util.Locale;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CredencialEmpleadoService {

    private final CuentaEmpleadoRepository cuentaEmpleadoRepository;
    private final CredencialEmpleadoRepository credencialEmpleadoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public CredencialEmpleadoService(
        CuentaEmpleadoRepository cuentaEmpleadoRepository,
        CredencialEmpleadoRepository credencialEmpleadoRepository,
        EmpleadoRepository empleadoRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.cuentaEmpleadoRepository = cuentaEmpleadoRepository;
        this.credencialEmpleadoRepository = credencialEmpleadoRepository;
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CuentaEmpleado createAccountWithCredential(String email, String empleadoClave, String plainPassword) {
        String normalizedEmail = normalizeEmail(email);
        if (cuentaEmpleadoRepository.existsByCorreoIgnoreCase(normalizedEmail)) {
            throw new ValidationException("Ya existe una cuenta para el correo: " + normalizedEmail);
        }

        Empleado empleado = empleadoRepository.findById(empleadoClave)
            .orElseThrow(() -> new EmpleadoNotFoundException(empleadoClave));

        CuentaEmpleado cuentaEmpleado = new CuentaEmpleado();
        cuentaEmpleado.setCorreo(normalizedEmail);
        cuentaEmpleado.setEmpleado(empleado);

        CuentaEmpleado cuentaGuardada = cuentaEmpleadoRepository.save(cuentaEmpleado);

        CredencialEmpleado credencialEmpleado = new CredencialEmpleado();
        credencialEmpleado.setCuentaEmpleado(cuentaGuardada);
        credencialEmpleado.setPasswordHash(passwordEncoder.encode(plainPassword));
        credencialEmpleadoRepository.save(credencialEmpleado);

        return cuentaGuardada;
    }

    public Optional<CredencialEmpleado> findCredentialByEmail(String email) {
        return credencialEmpleadoRepository.findByCuentaEmpleadoCorreoIgnoreCase(normalizeEmail(email));
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}