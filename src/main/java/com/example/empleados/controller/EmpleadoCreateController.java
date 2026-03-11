package com.example.empleados.controller;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;
import com.example.empleados.service.EmpleadoCreateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoCreateController {

    private final EmpleadoCreateService createService;

    public EmpleadoCreateController(EmpleadoCreateService createService) {
        this.createService = createService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpleadoDtos.EmpleadoResponse create(@Valid @RequestBody EmpleadoDtos.EmpleadoCreateRequest request) {
        Empleado empleado = createService.create(request);
        return EmpleadoMapper.toResponse(empleado);
    }
}
