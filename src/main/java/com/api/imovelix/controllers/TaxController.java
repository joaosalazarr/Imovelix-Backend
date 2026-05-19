package com.api.imovelix.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.imovelix.dto.request.RegisterTaxRequest;
import com.api.imovelix.dto.request.UpdateTaxRequest;
import com.api.imovelix.dto.response.TaxDetailsResponse;
import com.api.imovelix.dto.response.TaxResponse;
import com.api.imovelix.services.TaxService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/taxes")
public class TaxController {
    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaxResponse create(@Valid @RequestBody RegisterTaxRequest request) {
        return taxService.create(request);
    }

    @GetMapping
    public List<TaxResponse> findAll() {
        return taxService.findAll();
    }

    @GetMapping("/{id}")
    public TaxDetailsResponse findDetailsById(@PathVariable @Positive Long id) {
        return taxService.findDetailsById(id);
    }

    @PutMapping("/{id}")
    public TaxResponse update(@PathVariable @Positive Long id, @Valid @RequestBody UpdateTaxRequest request) {
        return taxService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        taxService.delete(id);
    }
}
