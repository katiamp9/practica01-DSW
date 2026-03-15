package com.example.empleados.service;

import com.example.empleados.controller.dto.AuthDtos;
import com.example.empleados.domain.CredencialEmpleado;
import com.example.empleados.repository.CuentaEmpleadoRepository;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthLoginService {

    private final CuentaEmpleadoRepository cuentaEmpleadoRepository;
    private final CredencialEmpleadoService credencialEmpleadoService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationAuditService authenticationAuditService;

    public AuthLoginService(
        CuentaEmpleadoRepository cuentaEmpleadoRepository,
        CredencialEmpleadoService credencialEmpleadoService,
        PasswordEncoder passwordEncoder,
        AuthenticationAuditService authenticationAuditService
    ) {
        this.cuentaEmpleadoRepository = cuentaEmpleadoRepository;
        this.credencialEmpleadoService = credencialEmpleadoService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationAuditService = authenticationAuditService;
    }

    public boolean login(AuthDtos.LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        cuentaEmpleadoRepository.findByCorreoIgnoreCase(normalizedEmail)
            .orElseThrow(() -> {
                authenticationAuditService.logFailure(normalizedEmail, "EMAIL_NOT_FOUND");
                return new InvalidCredentialsException();
            });

        CredencialEmpleado credencial = credencialEmpleadoService.findCredentialByEmail(normalizedEmail)
            .orElseThrow(() -> {
                authenticationAuditService.logFailure(normalizedEmail, "ACCOUNT_INCONSISTENT");
                return new InvalidCredentialsException();
            });

        boolean matches = passwordEncoder.matches(request.password(), credencial.getPasswordHash());
        if (!matches) {
            authenticationAuditService.logFailure(normalizedEmail, "INVALID_PASSWORD");
            throw new InvalidCredentialsException();
        }

        authenticationAuditService.logSuccess(normalizedEmail);
        return true;
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}