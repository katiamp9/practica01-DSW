package com.example.empleados.service;

public final class Roles {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private Roles() {
    }

    public static String normalizeOrDefault(String role) {
        if (role == null || role.trim().isEmpty()) {
            return ROLE_USER;
        }

        String normalized = role.trim().toUpperCase();
        if (!ROLE_USER.equals(normalized) && !ROLE_ADMIN.equals(normalized)) {
            throw new ValidationException("rol no válido");
        }
        return normalized;
    }
}
