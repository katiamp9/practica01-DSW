package com.example.empleados.repository;

import com.example.empleados.domain.Empleado;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmpleadoRepository extends JpaRepository<Empleado, String> {

    @Query(value = "SELECT nextval('empleados_consecutivo_seq')", nativeQuery = true)
    Long nextConsecutivo();

    @Query("""
        select
            e.clave as clave,
            e.nombre as nombre,
            e.direccion as direccion,
            e.telefono as telefono,
            d.id as departamentoId,
            e.rol as rol,
            c.correo as email
        from Empleado e
        left join e.departamento d
        left join CuentaEmpleado c on c.empleado.clave = e.clave
        """)
    Page<EmpleadoListProjection> findAllWithEmail(Pageable pageable);

    @Query("""
        select
            e.clave as clave,
            e.nombre as nombre,
            e.direccion as direccion,
            e.telefono as telefono,
            d.id as departamentoId,
            e.rol as rol,
            c.correo as email
        from Empleado e
        left join e.departamento d
        left join CuentaEmpleado c on c.empleado.clave = e.clave
        where e.clave = :clave
        """)
    Optional<EmpleadoListProjection> findWithEmailByClave(String clave);

    long countByDepartamentoId(Long departamentoId);
}
