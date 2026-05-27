package com.api.imovelix.services.security.contracts;

public interface PasswordHasherPort {
    String hash(String rawPassword);

    boolean matches(String rawPassword, String storedHash);
}
