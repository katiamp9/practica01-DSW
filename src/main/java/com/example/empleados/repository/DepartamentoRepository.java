package com.example.empleados.repository;

import com.example.empleados.domain.Departamento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    @Query(
        value = """
            select
                d.id as id,
                d.nombre as nombre,
                count(e.clave) as totalEmpleados
            from Departamento d
            left join Empleado e on e.departamento.id = d.id
            group by d.id, d.nombre
            """,
        countQuery = "select count(d.id) from Departamento d"
    )
    Page<DepartamentoListaProjection> findAllWithTotalEmpleados(Pageable pageable);

    Optional<Departamento> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
