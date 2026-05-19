package com.api.imovelix.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.imovelix.dto.request.ListPropertiesRequest;
import com.api.imovelix.dto.request.RegisterPropertyRequest;
import com.api.imovelix.dto.request.UpdatePropertyRequest;
import com.api.imovelix.dto.response.PageResponse;
import com.api.imovelix.dto.response.PropertyDetailsResponse;
import com.api.imovelix.dto.response.PropertyResponse;
import com.api.imovelix.services.PropertyService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyResponse create(@Valid @RequestBody RegisterPropertyRequest request) {
        return propertyService.create(request);
    }

    @GetMapping
    public PageResponse<PropertyResponse> findAll(@Valid @ModelAttribute ListPropertiesRequest request) {
        return propertyService.findAll(request);
    }

    @GetMapping("/by-user/{userId}")
    public List<PropertyResponse> findByUser(@PathVariable @Positive Long userId) {
        return propertyService.findByUser(userId);
    }

    @GetMapping("/{id}")
    public PropertyDetailsResponse findDetailsById(@PathVariable @Positive Long id) {
        return propertyService.findDetailsById(id);
    }

    @PutMapping("/{id}")
    public PropertyResponse update(
        @PathVariable @Positive Long id,
        @Valid @RequestBody UpdatePropertyRequest request
    ) {
        return propertyService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        propertyService.delete(id);
    }
}
