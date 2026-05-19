package com.api.imovelix.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.imovelix.dto.request.RegisterSystemUserRequest;
import com.api.imovelix.dto.request.UpdateSystemUserRequest;
import com.api.imovelix.dto.response.SystemUserResponse;
import com.api.imovelix.services.SystemUserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/users")
public class SystemUserController {
    private final SystemUserService systemUserService;

    public SystemUserController(SystemUserService systemUserService) {
        this.systemUserService = systemUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SystemUserResponse create(@Valid @RequestBody RegisterSystemUserRequest request) {
        return systemUserService.create(request);
    }

    @GetMapping
    public List<SystemUserResponse> findAll() {
        return systemUserService.findAll();
    }

    @GetMapping("/{id}")
    public SystemUserResponse findById(@PathVariable @Positive Long id) {
        return systemUserService.findById(id);
    }

    @PutMapping("/{id}")
    public SystemUserResponse update(
        @PathVariable @Positive Long id,
        @Valid @RequestBody UpdateSystemUserRequest request
    ) {
        return systemUserService.update(id, request);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable @Positive Long id) {
        systemUserService.deactivate(id);
    }

}
