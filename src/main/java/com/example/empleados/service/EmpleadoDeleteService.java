package com.example.empleados.service;

import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoDeleteService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoDeleteService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void delete(String clave) {
        if (!empleadoRepository.existsById(clave)) {
            throw new EmpleadoNotFoundException(clave);
        }
        empleadoRepository.deleteById(clave);
    }
}
