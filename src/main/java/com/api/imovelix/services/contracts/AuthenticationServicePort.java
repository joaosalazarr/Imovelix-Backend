package com.api.imovelix.services.contracts;

import com.api.imovelix.dto.request.LoginRequest;
import com.api.imovelix.dto.request.UpdatePasswordRequest;
import com.api.imovelix.dto.request.VerifyMfaRequest;
import com.api.imovelix.dto.response.AuthInfoResponse;
import com.api.imovelix.dto.response.LoginResponse;
import com.api.imovelix.models.UserAuthentication;

public interface AuthenticationServicePort {
    LoginResponse login(LoginRequest request, String clientIp);

    LoginResponse verifyMfa(VerifyMfaRequest request);

    AuthInfoResponse getAuthInfo(Long authenticationId);

    void updatePassword(Long authenticationId, UpdatePasswordRequest request);

    UserAuthentication getEntity(Long authenticationId);
}
