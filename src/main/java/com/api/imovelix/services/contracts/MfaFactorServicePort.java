package com.api.imovelix.services.contracts;

import java.util.List;

import com.api.imovelix.dto.request.RegisterMfaFactorRequest;
import com.api.imovelix.dto.request.VerifyMfaRequest;
import com.api.imovelix.dto.response.MfaFactorResponse;
import com.api.imovelix.models.MfaFactor;

public interface MfaFactorServicePort {
    MfaFactorResponse create(RegisterMfaFactorRequest request);

    List<MfaFactorResponse> findByAuthentication(Long authenticationId);

    MfaFactorResponse activate(Long id, VerifyMfaRequest request);

    MfaFactorResponse deactivate(Long id);

    MfaFactor getEntity(Long id);
}
