package com.example.empleados.controller;

import com.example.empleados.service.EmpleadoDeleteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoDeleteController {

    private final EmpleadoDeleteService deleteService;

    public EmpleadoDeleteController(EmpleadoDeleteService deleteService) {
        this.deleteService = deleteService;
    }

    @DeleteMapping("/{clave}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String clave) {
        deleteService.delete(clave);
    }
}
