package com.api.imovelix.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.imovelix.dto.request.RegisterTaxCalculationRequest;
import com.api.imovelix.dto.response.TaxCalculationResponse;
import com.api.imovelix.dto.response.TaxCalculationSummaryResponse;
import com.api.imovelix.services.TaxCalculationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/tax-calculations")
public class TaxCalculationController {
    private final TaxCalculationService taxCalculationService;

    public TaxCalculationController(TaxCalculationService taxCalculationService) {
        this.taxCalculationService = taxCalculationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaxCalculationResponse create(@Valid @RequestBody RegisterTaxCalculationRequest request) {
        return taxCalculationService.create(request);
    }

    @GetMapping("/by-property/{propertyId}")
    public List<TaxCalculationSummaryResponse> findByProperty(@PathVariable @Positive Long propertyId) {
        return taxCalculationService.findByProperty(propertyId);
    }

    @GetMapping("/{id}")
    public TaxCalculationResponse findById(@PathVariable @Positive Long id) {
        return taxCalculationService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        taxCalculationService.delete(id);
    }
}
