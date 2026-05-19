package com.api.imovelix.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.imovelix.dto.request.LoginRequest;
import com.api.imovelix.dto.request.UpdatePasswordRequest;
import com.api.imovelix.dto.response.AuthInfoResponse;
import com.api.imovelix.dto.response.LoginResponse;
import com.api.imovelix.services.AuthenticationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @GetMapping("/{authenticationId}")
    public AuthInfoResponse getAuthInfo(@PathVariable @Positive Long authenticationId) {
        return authenticationService.getAuthInfo(authenticationId);
    }

    @PatchMapping("/{authenticationId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
        @PathVariable @Positive Long authenticationId,
        @Valid @RequestBody UpdatePasswordRequest request
    ) {
        authenticationService.updatePassword(authenticationId, request);
    }
}
