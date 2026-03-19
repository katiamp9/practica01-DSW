package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.CuentaEmpleado;
import com.example.empleados.domain.Departamento;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpleadoCreateServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private EmpleadoValidationService validationService;

    @Mock
    private DepartamentoValidationService departamentoValidationService;

    @Mock
    private ClaveEmpleadoGenerator claveGenerator;

    @Mock
    private CredencialEmpleadoService credencialEmpleadoService;

    private EmpleadoCreateService createService;

    private Departamento departamento;

    @BeforeEach
    void setUp() {
        createService = new EmpleadoCreateService(
            empleadoRepository,
            validationService,
            departamentoValidationService,
            claveGenerator,
            credencialEmpleadoService
        );

        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Sistemas");

        when(departamentoValidationService.requireDepartamento(1L)).thenReturn(departamento);
    }

    @Test
    void create_shouldGenerateAndPersistEmpleado() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            " Ana ",
            " Calle 1 ",
            " 555-0101 ",
            1L,
            null
        );

        when(claveGenerator.generate()).thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1001L, "EMP-1001"));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Empleado result = createService.create(request);

        assertEquals("EMP-1001", result.getClave());
        assertEquals("EMP-", result.getPrefijo());
        assertEquals(1001L, result.getConsecutivo());
        assertEquals("Ana", result.getNombre());
        assertEquals(1L, result.getDepartamento().getId());

        ArgumentCaptor<Empleado> captor = ArgumentCaptor.forClass(Empleado.class);
        verify(empleadoRepository).save(captor.capture());
        assertEquals("Calle 1", captor.getValue().getDireccion());
        verify(validationService).validateCreate(request);
        verifyNoInteractions(credencialEmpleadoService);
    }

    @Test
    void create_shouldRetryAndSucceedWhenCollisionIsTransient() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            1L,
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
        verifyNoInteractions(credencialEmpleadoService);
    }

    @Test
    void create_shouldThrowClaveCollisionWhenRepositoryFailsAfterMaxRetries() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            1L,
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
        verifyNoInteractions(credencialEmpleadoService);
    }

    @Test
    void create_shouldCreateAccountWhenEmailAndPasswordAreProvided() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            1L,
            null,
            "Admin@Empresa.com",
            "admin123"
        );

        when(claveGenerator.generate()).thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1001L, "EMP-1001"));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(credencialEmpleadoService.createAccountWithCredential(eq("Admin@Empresa.com"), eq("EMP-1001"), eq("admin123")))
            .thenReturn(new CuentaEmpleado());

        Empleado result = createService.create(request);

        assertEquals("EMP-1001", result.getClave());
        verify(credencialEmpleadoService).createAccountWithCredential("Admin@Empresa.com", "EMP-1001", "admin123");
    }

    @Test
    void create_shouldFailWhenEmailAlreadyExists() {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest(
            "Ana",
            "Calle 1",
            "555-0101",
            1L,
            null,
            "admin@empresa.com",
            "admin123"
        );

        when(claveGenerator.generate()).thenReturn(new ClaveEmpleadoGenerator.ClaveGenerada("EMP-", 1001L, "EMP-1001"));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(credencialEmpleadoService.createAccountWithCredential(eq("admin@empresa.com"), eq("EMP-1001"), eq("admin123")))
            .thenThrow(new ValidationException("Ya existe una cuenta para el correo: admin@empresa.com"));

        ValidationException exception = assertThrows(ValidationException.class, () -> createService.create(request));
        assertEquals("Ya existe una cuenta para el correo: admin@empresa.com", exception.getMessage());
    }
}
