package com.api.imovelix.services.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public void assertAllowed(String key) {
        LoginAttempt attempt = attempts.get(key);
        if (attempt == null) {
            return;
        }

        if (attempt.lockedUntil() != null && attempt.lockedUntil().isAfter(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many login attempts. Try again later");
        }

        if (attempt.lockedUntil() != null) {
            attempts.remove(key);
        }
    }

    public void recordFailure(String key) {
        attempts.compute(key, (ignored, current) -> {
            int failures = current == null ? 1 : current.failures() + 1;
            Instant lockedUntil = failures >= MAX_ATTEMPTS ? Instant.now().plus(LOCK_DURATION) : null;
            return new LoginAttempt(failures, lockedUntil);
        });
    }

    public void recordSuccess(String key) {
        attempts.remove(key);
    }

    private record LoginAttempt(
        int failures,
        Instant lockedUntil
    ) {}
}
