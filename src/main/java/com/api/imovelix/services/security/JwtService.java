package com.api.imovelix.services.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.api.imovelix.models.UserAuthentication;

@Component
public class JwtService {
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final Pattern NUMBER_CLAIM_PATTERN = Pattern.compile("\"%s\"\\s*:\\s*(\\d+)");

    private final String secret;
    private final long expirationSeconds;

    public JwtService(
        @Value("${security.jwt.secret:${JWT_SECRET:dev-secret-change-me}}") String secret,
        @Value("${security.jwt.expiration-seconds:${JWT_EXPIRATION_SECONDS:86400}}") long expirationSeconds
    ) {
        if (secret == null || secret.length() < 16) {
            throw new IllegalStateException("JWT secret must have at least 16 characters");
        }
        this.secret = secret;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(UserAuthentication authentication) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationSeconds);

        String header = encodeJson(Map.of("alg", "HS256", "typ", "JWT"));
        String payload = encodeJson(Map.of(
            "sub", authentication.getId(),
            "email", authentication.getEmail(),
            "userId", authentication.getUser().getId(),
            "iat", now.getEpochSecond(),
            "exp", expiresAt.getEpochSecond()
        ));

        String unsignedToken = header + "." + payload;
        return unsignedToken + "." + sign(unsignedToken);
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public boolean isValid(String token) {
        try {
            String payload = verifiedPayload(token);
            long expiresAt = extractLongClaim(payload, "exp");
            return Instant.now().getEpochSecond() < expiresAt;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public Long extractAuthenticationId(String token) {
        return extractLongClaim(verifiedPayload(token), "sub");
    }

    public Long extractUserId(String token) {
        return extractLongClaim(verifiedPayload(token), "userId");
    }

    private String encodeJson(Map<String, ?> values) {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        values.forEach((key, value) -> joiner.add("\"" + escape(key) + "\":" + toJsonValue(value)));
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(joiner.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String toJsonValue(Object value) {
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        return "\"" + escape(String.valueOf(value)) + "\"";
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String sign(String unsignedToken) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(mac.doFinal(unsignedToken.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to generate JWT", exception);
        }
    }

    private String verifiedPayload(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is required");
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        String unsignedToken = parts[0] + "." + parts[1];
        if (!constantTimeEquals(sign(unsignedToken), parts[2])) {
            throw new IllegalArgumentException("Invalid JWT signature");
        }

        return new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
    }

    private Long extractLongClaim(String payload, String claim) {
        Matcher matcher = Pattern.compile(NUMBER_CLAIM_PATTERN.pattern().formatted(Pattern.quote(claim))).matcher(payload);
        if (!matcher.find()) {
            throw new IllegalArgumentException("JWT claim not found: " + claim);
        }
        return Long.valueOf(matcher.group(1));
    }

    private boolean constantTimeEquals(String expected, String actual) {
        byte[] expectedBytes = expected.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = actual.getBytes(StandardCharsets.UTF_8);
        if (expectedBytes.length != actualBytes.length) {
            return false;
        }

        int result = 0;
        for (int index = 0; index < expectedBytes.length; index++) {
            result |= expectedBytes[index] ^ actualBytes[index];
        }

        return result == 0;
    }
}
