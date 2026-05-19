package com.api.imovelix.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.dto.request.RegisterMfaFactorRequest;
import com.api.imovelix.dto.response.MfaFactorResponse;
import com.api.imovelix.mappers.MfaFactorMapper;
import com.api.imovelix.models.MfaFactor;
import com.api.imovelix.models.UserAuthentication;
import com.api.imovelix.repositories.MfaFactorRepository;
import com.api.imovelix.repositories.UserAuthenticationRepository;

@Service
public class MfaFactorService {
    private final SecureRandom secureRandom = new SecureRandom();

    private final MfaFactorRepository mfaFactorRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final MfaFactorMapper mfaFactorMapper;

    public MfaFactorService(
        MfaFactorRepository mfaFactorRepository,
        UserAuthenticationRepository userAuthenticationRepository,
        MfaFactorMapper mfaFactorMapper
    ) {
        this.mfaFactorRepository = mfaFactorRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.mfaFactorMapper = mfaFactorMapper;
    }

    @Transactional
    public MfaFactorResponse create(RegisterMfaFactorRequest request) {
        UserAuthentication authentication = userAuthenticationRepository.findById(request.userAuthenticationId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Authentication not found"));

        MfaFactor mfaFactor = mfaFactorMapper.toEntity(request, generateSecret(), authentication);
        return mfaFactorMapper.toResponse(mfaFactorRepository.save(mfaFactor));
    }

    @Transactional(readOnly = true)
    public List<MfaFactorResponse> findByAuthentication(Long authenticationId) {
        return mfaFactorRepository.findByUserAuthenticationId(authenticationId)
            .stream()
            .map(mfaFactorMapper::toResponse)
            .toList();
    }

    @Transactional
    public MfaFactorResponse activate(Long id) {
        MfaFactor factor = getEntity(id);
        factor.setActive(true);
        factor.setConfirmedAt(LocalDateTime.now());
        factor.getUserAuthentication().setMfaEnabled(true);
        return mfaFactorMapper.toResponse(mfaFactorRepository.save(factor));
    }

    @Transactional
    public MfaFactorResponse deactivate(Long id) {
        MfaFactor factor = getEntity(id);
        factor.setActive(false);
        factor.getUserAuthentication().setMfaEnabled(hasOtherActiveFactor(factor));
        return mfaFactorMapper.toResponse(mfaFactorRepository.save(factor));
    }

    public MfaFactor getEntity(Long id) {
        return mfaFactorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MFA factor not found"));
    }

    private boolean hasOtherActiveFactor(MfaFactor currentFactor) {
        return mfaFactorRepository.findByUserAuthenticationId(currentFactor.getUserAuthentication().getId())
            .stream()
            .anyMatch(factor -> !factor.getId().equals(currentFactor.getId()) && Boolean.TRUE.equals(factor.getActive()));
    }

    private String generateSecret() {
        byte[] secret = new byte[32];
        secureRandom.nextBytes(secret);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(secret);
    }
}
