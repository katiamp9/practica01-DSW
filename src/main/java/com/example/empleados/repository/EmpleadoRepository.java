package com.example.empleados.repository;

import com.example.empleados.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmpleadoRepository extends JpaRepository<Empleado, String> {

    @Query(value = "SELECT nextval('empleados_consecutivo_seq')", nativeQuery = true)
    Long nextConsecutivo();

    long countByDepartamentoId(Long departamentoId);
}
