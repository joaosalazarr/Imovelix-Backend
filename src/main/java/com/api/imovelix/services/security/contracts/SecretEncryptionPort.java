package com.api.imovelix.services.security.contracts;

public interface SecretEncryptionPort {
    String encrypt(String value);

    String decrypt(String value);
}
