package com.example.empleados.service;

import com.example.empleados.controller.dto.DepartamentoDtos;
import com.example.empleados.domain.Departamento;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.DepartamentoListaProjection;
import com.example.empleados.repository.EmpleadoRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    private DepartamentoService departamentoService;

    @BeforeEach
    void setUp() {
        departamentoService = new DepartamentoService(departamentoRepository, empleadoRepository);
    }

    @Test
    void create_shouldPersistDepartmentWithTrimmedName() {
        DepartamentoDtos.DepartamentoRequest request = new DepartamentoDtos.DepartamentoRequest("  Sistemas  ");

        when(departamentoRepository.findByNombreIgnoreCase("Sistemas")).thenReturn(Optional.empty());
        when(departamentoRepository.save(any(Departamento.class))).thenAnswer(invocation -> {
            Departamento departamento = invocation.getArgument(0);
            departamento.setId(1L);
            return departamento;
        });

        Departamento created = departamentoService.create(request);

        assertEquals(1L, created.getId());
        assertEquals("Sistemas", created.getNombre());
    }

    @Test
    void create_shouldFailWhenNameExists() {
        DepartamentoDtos.DepartamentoRequest request = new DepartamentoDtos.DepartamentoRequest("Sistemas");
        Departamento existing = departamento(1L, "Sistemas");

        when(departamentoRepository.findByNombreIgnoreCase("Sistemas")).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> departamentoService.create(request));
        assertEquals("Ya existe departamento con nombre: Sistemas", exception.getMessage());
    }

    @Test
    void create_shouldFailWhenNameIsBlank() {
        DepartamentoDtos.DepartamentoRequest request = new DepartamentoDtos.DepartamentoRequest("   ");

        ValidationException exception = assertThrows(ValidationException.class, () -> departamentoService.create(request));
        assertEquals("nombre es obligatorio", exception.getMessage());
    }

    @Test
    void update_shouldFailWhenDepartmentDoesNotExist() {
        when(departamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DepartamentoNotFoundException.class, () -> departamentoService.update(99L, new DepartamentoDtos.DepartamentoRequest("RH")));
    }

    @Test
    void update_shouldPersistTrimmedNameWhenValid() {
        Departamento current = departamento(1L, "Sistemas");

        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(current));
        when(departamentoRepository.findByNombreIgnoreCase("Sistemas TI")).thenReturn(Optional.empty());
        when(departamentoRepository.save(any(Departamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Departamento updated = departamentoService.update(1L, new DepartamentoDtos.DepartamentoRequest("  Sistemas TI  "));

        assertEquals(1L, updated.getId());
        assertEquals("Sistemas TI", updated.getNombre());
    }

    @Test
    void update_shouldFailWhenNameExistsInAnotherDepartment() {
        Departamento current = departamento(1L, "Sistemas");
        Departamento other = departamento(2L, "Finanzas");

        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(current));
        when(departamentoRepository.findByNombreIgnoreCase("Finanzas")).thenReturn(Optional.of(other));

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> departamentoService.update(1L, new DepartamentoDtos.DepartamentoRequest("Finanzas"))
        );

        assertEquals("Ya existe departamento con nombre: Finanzas", exception.getMessage());
    }

    @Test
    void delete_shouldFailWhenHasAssociatedEmployees() {
        when(departamentoRepository.existsById(1L)).thenReturn(true);
        when(empleadoRepository.countByDepartamentoId(1L)).thenReturn(2L);

        assertThrows(DepartamentoInUseException.class, () -> departamentoService.delete(1L));
    }

    @Test
    void delete_shouldFailWhenDepartmentDoesNotExist() {
        when(departamentoRepository.existsById(999L)).thenReturn(false);

        assertThrows(DepartamentoNotFoundException.class, () -> departamentoService.delete(999L));
    }

    @Test
    void delete_shouldDeleteWhenNoAssociatedEmployees() {
        when(departamentoRepository.existsById(1L)).thenReturn(true);
        when(empleadoRepository.countByDepartamentoId(1L)).thenReturn(0L);

        departamentoService.delete(1L);

        verify(departamentoRepository).deleteById(1L);
    }

    @Test
    void findAll_shouldReturnPagedResult() {
        var pageable = PageRequest.of(0, 20);
        when(departamentoRepository.findAllWithTotalEmpleados(pageable))
            .thenReturn(new PageImpl<>(java.util.List.of(projection(1L, "Sistemas", 3L)), pageable, 1));

        var page = departamentoService.findAll(pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals("Sistemas", page.getContent().get(0).getNombre());
        assertEquals(3L, page.getContent().get(0).getTotalEmpleados());
    }

    private Departamento departamento(Long id, String nombre) {
        Departamento departamento = new Departamento();
        departamento.setId(id);
        departamento.setNombre(nombre);
        return departamento;
    }

    private DepartamentoListaProjection projection(Long id, String nombre, Long totalEmpleados) {
        return new DepartamentoListaProjection() {
            @Override
            public Long getId() {
                return id;
            }

            @Override
            public String getNombre() {
                return nombre;
            }

            @Override
            public Long getTotalEmpleados() {
                return totalEmpleados;
            }
        };
    }
}
