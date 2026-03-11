package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpleadoCreateServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private EmpleadoValidationService validationService;

    @Mock
    private ClaveEmpleadoGenerator claveGenerator;

    private EmpleadoCreateService createService;

    @BeforeEach
    void setUp() {
        createService = new EmpleadoCreateService(empleadoRepository, validationService, claveGenerator);
    }

    @Test
    void create_shouldGenerateAndPersistEmpleado() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            " Ana ",
            " Calle 1 ",
            " 555-0101 ",
            null
        );

        when(claveGenerator.generate()).thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1001L, "EMP-1001"));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Empleado result = createService.create(request);

        assertEquals("EMP-1001", result.getClave());
        assertEquals("EMP-", result.getPrefijo());
        assertEquals(1001L, result.getConsecutivo());
        assertEquals("Ana", result.getNombre());

        ArgumentCaptor<Empleado> captor = ArgumentCaptor.forClass(Empleado.class);
        verify(empleadoRepository).save(captor.capture());
        assertEquals("Calle 1", captor.getValue().getDireccion());
        verify(validationService).validateCreate(request);
    }

    @Test
    void create_shouldRetryAndSucceedWhenCollisionIsTransient() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            null
        );

        when(claveGenerator.generate())
            .thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1001L, "EMP-1001"))
            .thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1002L, "EMP-1002"))
            .thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1003L, "EMP-1003"));

        when(empleadoRepository.save(any(Empleado.class)))
            .thenThrow(new DataIntegrityViolationException("duplicate"))
            .thenThrow(new DataIntegrityViolationException("duplicate"))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Empleado result = createService.create(request);

        assertEquals("EMP-1003", result.getClave());
        verify(claveGenerator, times(3)).generate();
        verify(empleadoRepository, times(3)).save(any(Empleado.class));
    }

    @Test
    void create_shouldThrowClaveCollisionWhenRepositoryFailsAfterMaxRetries() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            null
        );

        when(claveGenerator.generate())
            .thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1001L, "EMP-1001"))
            .thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1002L, "EMP-1002"))
            .thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1003L, "EMP-1003"));

        when(empleadoRepository.save(any(Empleado.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        ClaveCollisionException exception = assertThrows(ClaveCollisionException.class, () -> createService.create(request));
        assertEquals("Colisión de clave generada: EMP-1003", exception.getMessage());
        verify(claveGenerator, times(3)).generate();
        verify(empleadoRepository, times(3)).save(any(Empleado.class));
    }
}
