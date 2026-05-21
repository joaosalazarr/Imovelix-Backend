package com.api.imovelix.services.security;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class TotpService {
    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final long TIME_STEP_SECONDS = 30;
    private static final int CODE_DIGITS = 6;
    private static final int ALLOWED_TIME_WINDOWS = 1;

    public boolean verify(String base64Secret, String code) {
        if (base64Secret == null || code == null || !code.matches("\\d{6}")) {
            return false;
        }

        long timeWindow = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;
        for (int offset = -ALLOWED_TIME_WINDOWS; offset <= ALLOWED_TIME_WINDOWS; offset++) {
            if (code.equals(generate(base64Secret, timeWindow + offset))) {
                return true;
            }
        }

        return false;
    }

    private String generate(String base64Secret, long timeWindow) {
        try {
            byte[] secret = Base64.getUrlDecoder().decode(base64Secret);
            byte[] counter = ByteBuffer.allocate(Long.BYTES).putLong(timeWindow).array();
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            byte[] hash = mac.doFinal(counter);
            int offset = hash[hash.length - 1] & 0x0F;
            int binary = ((hash[offset] & 0x7F) << 24)
                | ((hash[offset + 1] & 0xFF) << 16)
                | ((hash[offset + 2] & 0xFF) << 8)
                | (hash[offset + 3] & 0xFF);
            int otp = binary % (int) Math.pow(10, CODE_DIGITS);
            return String.format("%06d", otp);
        } catch (IllegalArgumentException | GeneralSecurityException exception) {
            return "";
        }
    }
}
