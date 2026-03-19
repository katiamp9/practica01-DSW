package com.example.empleados.service;

import com.example.empleados.controller.dto.DepartamentoDtos;
import com.example.empleados.domain.Departamento;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository, EmpleadoRepository empleadoRepository) {
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional(readOnly = true)
    public Page<Departamento> findAll(Pageable pageable) {
        return departamentoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Departamento findById(Long id) {
        return departamentoRepository.findById(id)
            .orElseThrow(() -> new DepartamentoNotFoundException(id));
    }

    @Transactional
    public Departamento create(DepartamentoDtos.DepartamentoRequest request) {
        String nombre = normalizeNombre(request.nombre());
        validateNombreDisponible(nombre, null);

        Departamento departamento = new Departamento();
        departamento.setNombre(nombre);
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public Departamento update(Long id, DepartamentoDtos.DepartamentoRequest request) {
        Departamento departamento = departamentoRepository.findById(id)
            .orElseThrow(() -> new DepartamentoNotFoundException(id));

        String nombre = normalizeNombre(request.nombre());
        validateNombreDisponible(nombre, id);

        departamento.setNombre(nombre);
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public void delete(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNotFoundException(id);
        }

        long empleadosAsociados = empleadoRepository.countByDepartamentoId(id);
        if (empleadosAsociados > 0) {
            throw new DepartamentoInUseException(id);
        }

        departamentoRepository.deleteById(id);
    }

    private String normalizeNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException("nombre es obligatorio");
        }

        String normalized = nombre.trim();
        if (normalized.length() > 100) {
            throw new ValidationException("nombre debe tener máximo 100 caracteres");
        }

        return normalized;
    }

    private void validateNombreDisponible(String nombre, Long currentId) {
        departamentoRepository.findByNombreIgnoreCase(nombre)
            .ifPresent(existing -> {
                if (currentId == null || !existing.getId().equals(currentId)) {
                    throw new ValidationException("Ya existe departamento con nombre: " + nombre);
                }
            });
    }
}
