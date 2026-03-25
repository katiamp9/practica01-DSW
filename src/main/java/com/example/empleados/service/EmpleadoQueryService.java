package com.example.empleados.service;

import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.repository.EmpleadoListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoQueryService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoQueryService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Page<EmpleadoListProjection> findAll(Pageable pageable) {
        return empleadoRepository.findAllWithEmail(pageable);
    }

    public EmpleadoListProjection findByClave(String clave) {
        return empleadoRepository.findWithEmailByClave(clave)
            .orElseThrow(() -> new EmpleadoNotFoundException(clave));
    }
}
