package com.example.empleados.service;

import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoQueryService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoQueryService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Page<Empleado> findAll(Pageable pageable) {
        return empleadoRepository.findAll(pageable);
    }

    public Empleado findByClave(String clave) {
        return empleadoRepository.findById(clave)
            .orElseThrow(() -> new EmpleadoNotFoundException(clave));
    }
}
