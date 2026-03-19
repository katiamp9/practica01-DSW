package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpleadoUpdateServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private EmpleadoValidationService validationService;

    @Mock
    private DepartamentoValidationService departamentoValidationService;

    @Mock
    private CredencialEmpleadoService credencialEmpleadoService;

    private EmpleadoUpdateService updateService;

    private Departamento departamento;

    @BeforeEach
    void setUp() {
        updateService = new EmpleadoUpdateService(
            empleadoRepository,
            validationService,
            departamentoValidationService,
            credencialEmpleadoService
        );

        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Sistemas");

    }

    @Test
    void update_shouldUpdateOnlyEmployeeDataWhenAccessFieldsAreMissing() {
        Empleado empleado = empleado("EMP-1001", "Ana", "Calle 1", "555-0101", 1L, "Sistemas");
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest("Ana Mod", "Calle 2", "555-0202");

        when(empleadoRepository.findById("EMP-1001")).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Empleado result = updateService.update("EMP-1001", request);

        assertEquals("Ana Mod", result.getNombre());
        assertEquals("Calle 2", result.getDireccion());
        assertEquals("555-0202", result.getTelefono());
        assertEquals(1L, result.getDepartamento().getId());
        verify(credencialEmpleadoService, never()).upsertAccessForEmpleado(any(), any(), any());
    }

    @Test
    void update_shouldActivateAccessWhenEmailAndPasswordAreProvided() {
        Empleado empleado = empleado("EMP-1001", "Ana", "Calle 1", "555-0101", 1L, "Sistemas");
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            "Ana Mod",
            "Calle 2",
            "555-0202",
            "Admin@Empresa.com",
            "admin123"
        );

        when(empleadoRepository.findById("EMP-1001")).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Empleado result = updateService.update("EMP-1001", request);

        assertEquals("Ana Mod", result.getNombre());
        verify(credencialEmpleadoService).upsertAccessForEmpleado("EMP-1001", "Admin@Empresa.com", "admin123");
    }

    @Test
    void update_shouldKeepEmployeeFieldsWhenOnlyAccessFieldsAreProvided() {
        Empleado empleado = empleado("EMP-1001", "Ana", "Calle 1", "555-0101", 1L, "Sistemas");
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            null,
            null,
            null,
            "Admin@Empresa.com",
            "admin123"
        );

        when(empleadoRepository.findById("EMP-1001")).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Empleado result = updateService.update("EMP-1001", request);

        assertEquals("Ana", result.getNombre());
        assertEquals("Calle 1", result.getDireccion());
        assertEquals("555-0101", result.getTelefono());
        verify(credencialEmpleadoService).upsertAccessForEmpleado("EMP-1001", "Admin@Empresa.com", "admin123");
    }

    @Test
    void update_shouldFailAndRollbackWhenEmailIsDuplicated() {
        Empleado empleado = empleado("EMP-1001", "Ana", "Calle 1", "555-0101", 1L, "Sistemas");
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            "Ana Mod",
            "Calle 2",
            "555-0202",
            "admin@empresa.com",
            "admin123"
        );

        when(empleadoRepository.findById("EMP-1001")).thenReturn(Optional.of(empleado));
        org.mockito.Mockito.doThrow(new ValidationException("Ya existe una cuenta para el correo: admin@empresa.com"))
            .when(credencialEmpleadoService)
            .upsertAccessForEmpleado(eq("EMP-1001"), eq("admin@empresa.com"), eq("admin123"));

        ValidationException exception = assertThrows(ValidationException.class, () -> updateService.update("EMP-1001", request));

        assertEquals("Ya existe una cuenta para el correo: admin@empresa.com", exception.getMessage());
    }

    @Test
    void update_shouldUpdateDepartamentoWhenDepartamentoIdIsProvided() {
        Empleado empleado = empleado("EMP-1001", "Ana", "Calle 1", "555-0101", 1L, "Sistemas");
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            2L,
            null,
            null
        );

        when(empleadoRepository.findById("EMP-1001")).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(departamentoValidationService.requireDepartamento(2L)).thenReturn(departamentoConId(2L, "RH"));

        Empleado result = updateService.update("EMP-1001", request);

        assertEquals(2L, result.getDepartamento().getId());
        verify(departamentoValidationService).requireDepartamento(2L);
    }

    private Empleado empleado(String clave, String nombre, String direccion, String telefono, Long departamentoId, String departamentoNombre) {
        Empleado empleado = new Empleado();
        empleado.setClave(clave);
        empleado.setNombre(nombre);
        empleado.setDireccion(direccion);
        empleado.setTelefono(telefono);
        empleado.setDepartamento(departamentoConId(departamentoId, departamentoNombre));
        return empleado;
    }

    private Departamento departamentoConId(Long id, String nombre) {
        Departamento value = new Departamento();
        value.setId(id);
        value.setNombre(nombre);
        return value;
    }
}