package com.example.empleados.controller;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.service.EmpleadoUpdateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoUpdateController {

    private final EmpleadoUpdateService updateService;

    public EmpleadoUpdateController(EmpleadoUpdateService updateService) {
        this.updateService = updateService;
    }

    @PutMapping("/{clave}")
    public EmpleadoDtos.EmpleadoResponse update(
        @PathVariable String clave,
        @Valid @RequestBody EmpleadoDtos.EmpleadoUpdateRequest request
    ) {
        return EmpleadoMapper.toResponse(updateService.update(clave, request));
    }
}
