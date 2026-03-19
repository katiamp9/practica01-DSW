package com.example.empleados.service;

import com.example.empleados.domain.Departamento;
import com.example.empleados.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoValidationService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoValidationService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public Departamento requireDepartamento(Long departamentoId) {
        if (departamentoId == null) {
            throw new ValidationException("departamentoId es obligatorio");
        }

        return departamentoRepository.findById(departamentoId)
            .orElseThrow(() -> new DepartamentoNotFoundException(departamentoId));
    }
}
