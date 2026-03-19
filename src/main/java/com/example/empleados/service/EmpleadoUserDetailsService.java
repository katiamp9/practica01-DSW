package com.example.empleados.service;

import com.example.empleados.domain.CuentaEmpleado;
import com.example.empleados.repository.CuentaEmpleadoRepository;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoUserDetailsService implements UserDetailsService {

    private final CuentaEmpleadoRepository cuentaEmpleadoRepository;
    private final String adminUsername;
    private final String adminPasswordHash;

    public EmpleadoUserDetailsService(
        CuentaEmpleadoRepository cuentaEmpleadoRepository,
        @Value("${SPRING_SECURITY_USER_NAME:admin}") String adminUsername,
        @Value("${SPRING_SECURITY_USER_PASSWORD_HASH:$2y$10$rDLkWjFUlUkWr9GIiK42OOLVqlFM1eBhAmDNxf4VTAQmV.p.JWO5i}") String adminPasswordHash
    ) {
        this.cuentaEmpleadoRepository = cuentaEmpleadoRepository;
        this.adminUsername = adminUsername;
        this.adminPasswordHash = adminPasswordHash;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalizedUsername = normalize(username);

        if (adminUsername.equalsIgnoreCase(normalizedUsername)) {
            return User.withUsername(adminUsername)
                .password(adminPasswordHash)
                .authorities(new SimpleGrantedAuthority(Roles.ROLE_ADMIN))
                .build();
        }

        CuentaEmpleado cuentaEmpleado = cuentaEmpleadoRepository.findWithEmpleadoByCorreoIgnoreCase(normalizedUsername)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (cuentaEmpleado.getCredencial() == null) {
            throw new UsernameNotFoundException("Usuario sin credencial");
        }

        String role = Roles.normalizeOrDefault(cuentaEmpleado.getEmpleado().getRol());
        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        return User.withUsername(cuentaEmpleado.getCorreo())
            .password(cuentaEmpleado.getCredencial().getPasswordHash())
            .authorities(authority)
            .build();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
