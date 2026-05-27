package com.api.imovelix.services.security.contracts;

public interface LoginAttemptPort {
    void assertAllowed(String key);

    void recordFailure(String key);

    void recordSuccess(String key);
}
