package com.example.empleados.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public interface AuthenticationAuditService {

    void logSuccess(String email);

    void logFailure(String email, String reasonCode);
}

@Service
class AuthenticationAuditServiceImpl implements AuthenticationAuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationAuditServiceImpl.class);

    @Override
    public void logSuccess(String email) {
        LOGGER.info("AUTH_SUCCESS email={}", maskEmail(email));
    }

    @Override
    public void logFailure(String email, String reasonCode) {
        LOGGER.warn("AUTH_FAILURE email={} reasonCode={}", maskEmail(email), reasonCode);
    }

    private String maskEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            return "***";
        }

        String[] parts = email.split("@", 2);
        String localPart = parts[0];
        String domain = parts[1];
        if (localPart.isEmpty()) {
            return "***@" + domain;
        }
        return localPart.charAt(0) + "***@" + domain;
    }
}