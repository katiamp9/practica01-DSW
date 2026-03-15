package com.example.empleados.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigBCryptTest {

    @Test
    void passwordEncoder_shouldUseBCrypt() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        String plain = "admin123";
        String hash = passwordEncoder.encode(plain);

        assertNotEquals(plain, hash);
        assertTrue(passwordEncoder.matches(plain, hash));
    }
}