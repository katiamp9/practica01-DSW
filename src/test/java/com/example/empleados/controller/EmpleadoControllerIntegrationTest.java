package com.example.empleados.controller;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.dto.EmpleadoDtos;
import com.example.empleados.domain.Empleado;
import com.example.empleados.service.EmpleadoCreateService;
import com.example.empleados.service.EmpleadoDeleteService;
import com.example.empleados.service.EmpleadoNotFoundException;
import com.example.empleados.service.EmpleadoQueryService;
import com.example.empleados.service.EmpleadoUpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
    EmpleadoCreateController.class,
    EmpleadoQueryController.class,
    EmpleadoUpdateController.class,
    EmpleadoDeleteController.class,
    ApiExceptionHandler.class
})
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
    "SPRING_SECURITY_USER_NAME=admin",
    "SPRING_SECURITY_USER_PASSWORD_HASH=$2y$10$rDLkWjFUlUkWr9GIiK42OOLVqlFM1eBhAmDNxf4VTAQmV.p.JWO5i"
})
class EmpleadoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmpleadoCreateService createService;

    @MockBean
    private EmpleadoQueryService queryService;

    @MockBean
    private EmpleadoUpdateService updateService;

    @MockBean
    private EmpleadoDeleteService deleteService;

    @Test
    void shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/empleados"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateEmpleadoWithGeneratedClave() throws Exception {
        Empleado empleado = empleado("EMP-1001", "Ana", "Calle 1", "555-0101");
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest("Ana", "Calle 1", "555-0101", null);

        when(createService.create(any())).thenReturn(empleado);

        mockMvc.perform(post("/api/v1/empleados")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.clave").value("EMP-1001"))
            .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void shouldReturnBadRequestWhenManualClaveSent() throws Exception {
        EmpleadoDtos.EmpleadoCreateRequest request = new EmpleadoDtos.EmpleadoCreateRequest("Ana", "Calle 1", "555-0101", "EMP-9999");
        when(createService.create(any())).thenThrow(new com.example.empleados.service.ValidationException("No se permite enviar clave manualmente"));

        mockMvc.perform(post("/api/v1/empleados")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void shouldListAndFindByClave() throws Exception {
        when(queryService.findAll(any())).thenReturn(
            new PageImpl<>(
                List.of(empleado("EMP-1001", "Ana", "Calle 1", "555-0101")),
                PageRequest.of(0, 20),
                1
            )
        );
        when(queryService.findByClave("EMP-1001")).thenReturn(empleado("EMP-1001", "Ana", "Calle 1", "555-0101"));

        mockMvc.perform(get("/api/v1/empleados")
                .param("page", "0")
                .param("sort", "nombre,asc")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].clave").value("EMP-1001"));

        mockMvc.perform(get("/api/v1/empleados/EMP-1001")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clave").value("EMP-1001"));
    }

    @Test
    void shouldRequirePaginationAndSortParamsForCollectionGet() throws Exception {
        mockMvc.perform(get("/api/v1/empleados")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value("UNPROCESSABLE_ENTITY"));
        }

        @Test
        void shouldReturnUnprocessableEntityForInvalidSortField() throws Exception {
        mockMvc.perform(get("/api/v1/empleados")
            .param("page", "0")
            .param("sort", "apellido,asc")
            .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value("UNPROCESSABLE_ENTITY"));
        }

        @Test
        void shouldReturnUnprocessableEntityForInvalidSize() throws Exception {
        mockMvc.perform(get("/api/v1/empleados")
            .param("page", "0")
            .param("size", "101")
            .param("sort", "nombre,asc")
            .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value("UNPROCESSABLE_ENTITY"));
    }

    @Test
    void shouldReturnNotFoundForMissingEmpleado() throws Exception {
        when(queryService.findByClave("EMP-9999")).thenThrow(new EmpleadoNotFoundException("EMP-9999"));

        mockMvc.perform(get("/api/v1/empleados/EMP-9999")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void shouldUpdateAndDeleteEmpleado() throws Exception {
        EmpleadoDtos.EmpleadoUpdateRequest request = new EmpleadoDtos.EmpleadoUpdateRequest("Ana Mod", "Calle 2", "555-1111");
        when(updateService.update(eq("EMP-1001"), any())).thenReturn(empleado("EMP-1001", "Ana Mod", "Calle 2", "555-1111"));
        doNothing().when(deleteService).delete("EMP-1001");

        mockMvc.perform(put("/api/v1/empleados/EMP-1001")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Ana Mod"));

        mockMvc.perform(delete("/api/v1/empleados/EMP-1001")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundOnDeleteMissing() throws Exception {
        doThrow(new EmpleadoNotFoundException("EMP-9999")).when(deleteService).delete("EMP-9999");

        mockMvc.perform(delete("/api/v1/empleados/EMP-9999")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    private Empleado empleado(String clave, String nombre, String direccion, String telefono) {
        Empleado empleado = new Empleado();
        empleado.setClave(clave);
        empleado.setNombre(nombre);
        empleado.setDireccion(direccion);
        empleado.setTelefono(telefono);
        return empleado;
    }
}
