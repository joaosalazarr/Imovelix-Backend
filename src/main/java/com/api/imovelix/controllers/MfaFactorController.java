package com.api.imovelix.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.imovelix.dto.request.RegisterMfaFactorRequest;
import com.api.imovelix.dto.response.MfaFactorResponse;
import com.api.imovelix.services.MfaFactorService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/mfa-factors")
public class MfaFactorController {
    private final MfaFactorService mfaFactorService;

    public MfaFactorController(MfaFactorService mfaFactorService) {
        this.mfaFactorService = mfaFactorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MfaFactorResponse create(@Valid @RequestBody RegisterMfaFactorRequest request) {
        return mfaFactorService.create(request);
    }

    @GetMapping("/by-authentication/{authenticationId}")
    public List<MfaFactorResponse> findByAuthentication(@PathVariable @Positive Long authenticationId) {
        return mfaFactorService.findByAuthentication(authenticationId);
    }

    @PatchMapping("/{id}/activate")
    public MfaFactorResponse activate(@PathVariable @Positive Long id) {
        return mfaFactorService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public MfaFactorResponse deactivate(@PathVariable @Positive Long id) {
        return mfaFactorService.deactivate(id);
    }
}
