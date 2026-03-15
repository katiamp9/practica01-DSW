package com.example.empleados.repository;

import com.example.empleados.domain.CuentaEmpleado;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaEmpleadoRepository extends JpaRepository<CuentaEmpleado, UUID> {

    Optional<CuentaEmpleado> findByCorreoIgnoreCase(String correo);

    Optional<CuentaEmpleado> findByEmpleadoClave(String empleadoClave);

    boolean existsByCorreoIgnoreCase(String correo);
}