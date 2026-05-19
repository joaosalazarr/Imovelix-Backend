package com.api.imovelix.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.dto.request.RegisterTaxRequest;
import com.api.imovelix.dto.request.UpdateTaxRequest;
import com.api.imovelix.dto.response.TaxDetailsResponse;
import com.api.imovelix.dto.response.TaxResponse;
import com.api.imovelix.mappers.TaxCalculationMapper;
import com.api.imovelix.mappers.TaxMapper;
import com.api.imovelix.models.Tax;
import com.api.imovelix.repositories.TaxCalculationRepository;
import com.api.imovelix.repositories.TaxRepository;

@Service
public class TaxService {
    private final TaxRepository taxRepository;
    private final TaxCalculationRepository taxCalculationRepository;
    private final TaxMapper taxMapper;
    private final TaxCalculationMapper taxCalculationMapper;

    public TaxService(
        TaxRepository taxRepository,
        TaxCalculationRepository taxCalculationRepository,
        TaxMapper taxMapper,
        TaxCalculationMapper taxCalculationMapper
    ) {
        this.taxRepository = taxRepository;
        this.taxCalculationRepository = taxCalculationRepository;
        this.taxMapper = taxMapper;
        this.taxCalculationMapper = taxCalculationMapper;
    }

    @Transactional
    public TaxResponse create(RegisterTaxRequest request) {
        return taxMapper.toResponse(taxRepository.save(taxMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public List<TaxResponse> findAll() {
        return taxRepository.findAll().stream().map(taxMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TaxDetailsResponse findDetailsById(Long id) {
        Tax tax = getEntity(id);
        return taxMapper.toDetailsResponse(
            tax,
            taxCalculationRepository.findByTaxId(id).stream().map(taxCalculationMapper::toSummaryResponse).toList()
        );
    }

    @Transactional
    public TaxResponse update(Long id, UpdateTaxRequest request) {
        Tax tax = getEntity(id);
        taxMapper.updateEntity(request, tax);
        return taxMapper.toResponse(taxRepository.save(tax));
    }

    @Transactional
    public void delete(Long id) {
        Tax tax = getEntity(id);
        taxRepository.delete(tax);
    }

    public Tax getEntity(Long id) {
        return taxRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax not found"));
    }
}
