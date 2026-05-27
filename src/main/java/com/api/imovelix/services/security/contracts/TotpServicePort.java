package com.api.imovelix.services.security.contracts;

public interface TotpServicePort {
    boolean verify(String base64Secret, String code);
}
