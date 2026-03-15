package com.example.empleados.repository;

import com.example.empleados.domain.CredencialEmpleado;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredencialEmpleadoRepository extends JpaRepository<CredencialEmpleado, UUID> {

    Optional<CredencialEmpleado> findByCuentaEmpleadoId(UUID cuentaEmpleadoId);

    Optional<CredencialEmpleado> findByCuentaEmpleadoCorreoIgnoreCase(String correo);
}