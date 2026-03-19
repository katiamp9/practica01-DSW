package com.example.empleados.controller;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.dto.DepartamentoDtos;
import com.example.empleados.domain.Departamento;
import com.example.empleados.service.DepartamentoInUseException;
import com.example.empleados.service.DepartamentoNotFoundException;
import com.example.empleados.service.DepartamentoService;
import com.example.empleados.service.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
    DepartamentoController.class,
    ApiExceptionHandler.class
})
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
    "SPRING_SECURITY_USER_NAME=admin",
    "SPRING_SECURITY_USER_PASSWORD_HASH=$2y$10$rDLkWjFUlUkWr9GIiK42OOLVqlFM1eBhAmDNxf4VTAQmV.p.JWO5i"
})
class DepartamentoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartamentoService departamentoService;

    @Test
    void shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/departamentos"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRequirePaginationAndSortParamsForCollectionGet() throws Exception {
        mockMvc.perform(get("/api/v1/departamentos")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value("UNPROCESSABLE_ENTITY"));
    }

    @Test
    void shouldListDepartamentosWithPaginationAndSort() throws Exception {
        when(departamentoService.findAll(any())).thenReturn(
            new PageImpl<>(
                List.of(departamento(1L, "Sistemas")),
                PageRequest.of(0, 20),
                1
            )
        );

        mockMvc.perform(get("/api/v1/departamentos")
                .param("page", "0")
                .param("sort", "nombre,asc")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].nombre").value("Sistemas"));
    }

    @Test
    void shouldCreateDepartamento() throws Exception {
        var request = new DepartamentoDtos.DepartamentoRequest("Finanzas");
        when(departamentoService.create(any())).thenReturn(departamento(10L, "Finanzas"));

        mockMvc.perform(post("/api/v1/departamentos")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.nombre").value("Finanzas"));
    }

    @Test
    void shouldGetDepartamentoById() throws Exception {
        when(departamentoService.findById(1L)).thenReturn(departamento(1L, "Sistemas"));

        mockMvc.perform(get("/api/v1/departamentos/1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Sistemas"));
    }

    @Test
    void shouldUpdateDepartamento() throws Exception {
        var request = new DepartamentoDtos.DepartamentoRequest("Sistemas TI");
        when(departamentoService.update(eq(1L), any())).thenReturn(departamento(1L, "Sistemas TI"));

        mockMvc.perform(put("/api/v1/departamentos/1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Sistemas TI"));
    }

    @Test
    void shouldDeleteDepartamento() throws Exception {
        doNothing().when(departamentoService).delete(1L);

        mockMvc.perform(delete("/api/v1/departamentos/1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnConflictWhenDeletingDepartmentInUse() throws Exception {
        doThrow(new DepartamentoInUseException(1L)).when(departamentoService).delete(1L);

        mockMvc.perform(delete("/api/v1/departamentos/1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("CONFLICT"));
    }

    @Test
    void shouldReturnNotFoundWhenDepartmentMissing() throws Exception {
        when(departamentoService.findById(999L)).thenThrow(new DepartamentoNotFoundException(999L));

        mockMvc.perform(get("/api/v1/departamentos/999")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        var request = new DepartamentoDtos.DepartamentoRequest("Sistemas");
        when(departamentoService.create(any())).thenThrow(new ValidationException("Ya existe departamento con nombre: Sistemas"));

        mockMvc.perform(post("/api/v1/departamentos")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    private Departamento departamento(Long id, String nombre) {
        Departamento departamento = new Departamento();
        departamento.setId(id);
        departamento.setNombre(nombre);
        return departamento;
    }
}
