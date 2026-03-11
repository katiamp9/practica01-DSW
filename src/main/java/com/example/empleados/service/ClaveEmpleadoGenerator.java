package com.example.empleados.service;

import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class ClaveEmpleadoGenerator {

    private static final String PREFIX = "EMP-";

    private final EmpleadoRepository empleadoRepository;

    public ClaveEmpleadoGenerator(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public ClaveGenerada generate() {
        Long consecutivo = empleadoRepository.nextConsecutivo();
        String clave = PREFIX + consecutivo;
        return new ClaveGenerada(PREFIX, consecutivo, clave);
    }

    public record ClaveGenerada(String prefijo, Long consecutivo, String clave) {
    }
}
