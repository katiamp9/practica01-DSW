package com.example.empleados.repository;

import com.example.empleados.domain.CredencialEmpleado;
import com.example.empleados.domain.CuentaEmpleado;
import com.example.empleados.domain.Empleado;
import com.example.empleados.service.CredencialEmpleadoService;
import com.example.empleados.service.ValidationException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "SPRING_SECURITY_USER_NAME=admin",
    "SPRING_SECURITY_USER_PASSWORD_HASH=$2y$10$rDLkWjFUlUkWr9GIiK42OOLVqlFM1eBhAmDNxf4VTAQmV.p.JWO5i",
    "INITIAL_ADMIN_PASSWORD_HASH=$2y$10$rDLkWjFUlUkWr9GIiK42OOLVqlFM1eBhAmDNxf4VTAQmV.p.JWO5i"
})
class CredencialEmpleadoRepositoryIntegrationTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private CredencialEmpleadoService credencialEmpleadoService;

    @Autowired
    private CredencialEmpleadoRepository credencialEmpleadoRepository;

    @Test
    void shouldPersistPasswordAsHashAndNeverAsPlainText() {
        Empleado empleado = new Empleado();
        empleado.setClave("EMP-2001");
        empleado.setPrefijo("EMP-");
        empleado.setConsecutivo(2001L);
        empleado.setNombre("Ana");
        empleado.setDireccion("Calle 1");
        empleado.setTelefono("555-0001");
        empleadoRepository.save(empleado);

        String plainPassword = "plain-secret";
        CuentaEmpleado cuentaEmpleado = credencialEmpleadoService.createAccountWithCredential(
            "admin@empresa.com",
            "EMP-2001",
            plainPassword
        );

        Optional<CredencialEmpleado> credencial = credencialEmpleadoRepository
            .findByCuentaEmpleadoCorreoIgnoreCase("ADMIN@EMPRESA.COM");

        assertTrue(credencial.isPresent());
        assertNotEquals(plainPassword, credencial.get().getPasswordHash());
        assertTrue(credencial.get().getPasswordHash().startsWith("$2"));
        assertTrue(credencial.get().getCuentaEmpleado().getId().equals(cuentaEmpleado.getId()));
    }

    @Test
    void shouldRejectDuplicateEmailCaseInsensitiveWhenCreatingAccount() {
        Empleado empleadoA = new Empleado();
        empleadoA.setClave("EMP-2002");
        empleadoA.setPrefijo("EMP-");
        empleadoA.setConsecutivo(2002L);
        empleadoA.setNombre("Luis");
        empleadoA.setDireccion("Calle 2");
        empleadoA.setTelefono("555-0002");
        empleadoRepository.save(empleadoA);

        Empleado empleadoB = new Empleado();
        empleadoB.setClave("EMP-2003");
        empleadoB.setPrefijo("EMP-");
        empleadoB.setConsecutivo(2003L);
        empleadoB.setNombre("Maria");
        empleadoB.setDireccion("Calle 3");
        empleadoB.setTelefono("555-0003");
        empleadoRepository.save(empleadoB);

        credencialEmpleadoService.createAccountWithCredential("admin@empresa.com", "EMP-2002", "pass-1");

        assertThrows(
            ValidationException.class,
            () -> credencialEmpleadoService.createAccountWithCredential("Admin@Empresa.com", "EMP-2003", "pass-2")
        );
    }
}