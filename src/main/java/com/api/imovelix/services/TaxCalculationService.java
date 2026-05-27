package com.api.imovelix.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.dto.request.RegisterTaxCalculationRequest;
import com.api.imovelix.dto.response.PaymentResponse;
import com.api.imovelix.dto.response.TaxCalculationResponse;
import com.api.imovelix.dto.response.TaxCalculationSummaryResponse;
import com.api.imovelix.mappers.PaymentMapper;
import com.api.imovelix.mappers.TaxCalculationMapper;
import com.api.imovelix.models.Property;
import com.api.imovelix.models.Tax;
import com.api.imovelix.models.TaxCalculation;
import com.api.imovelix.repositories.PaymentRepository;
import com.api.imovelix.repositories.TaxCalculationRepository;
import com.api.imovelix.services.contracts.PropertyServicePort;
import com.api.imovelix.services.contracts.TaxCalculationServicePort;
import com.api.imovelix.services.contracts.TaxServicePort;

@Service
public class TaxCalculationService implements TaxCalculationServicePort {
    private final TaxCalculationRepository taxCalculationRepository;
    private final PaymentRepository paymentRepository;
    private final PropertyServicePort propertyService;
    private final TaxServicePort taxService;
    private final TaxCalculationMapper taxCalculationMapper;
    private final PaymentMapper paymentMapper;

    public TaxCalculationService(
        TaxCalculationRepository taxCalculationRepository,
        PaymentRepository paymentRepository,
        PropertyServicePort propertyService,
        TaxServicePort taxService,
        TaxCalculationMapper taxCalculationMapper,
        PaymentMapper paymentMapper
    ) {
        this.taxCalculationRepository = taxCalculationRepository;
        this.paymentRepository = paymentRepository;
        this.propertyService = propertyService;
        this.taxService = taxService;
        this.taxCalculationMapper = taxCalculationMapper;
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    public TaxCalculationResponse create(RegisterTaxCalculationRequest request) {
        Property property = propertyService.getEntity(request.propertyId());
        propertyService.requirePropertyOwner(property);
        Tax tax = taxService.getEntity(request.taxId());
        TaxCalculation taxCalculation = taxCalculationRepository.save(taxCalculationMapper.toEntity(request, property, tax));

        return toResponse(taxCalculation);
    }

    @Transactional(readOnly = true)
    public List<TaxCalculationSummaryResponse> findByProperty(Long propertyId) {
        propertyService.requirePropertyOwner(propertyService.getEntity(propertyId));
        return taxCalculationRepository.findByPropertyId(propertyId)
            .stream()
            .map(taxCalculationMapper::toSummaryResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public TaxCalculationResponse findById(Long id) {
        TaxCalculation taxCalculation = getEntity(id);
        propertyService.requirePropertyOwner(taxCalculation.getProperty());
        return toResponse(taxCalculation);
    }

    @Transactional
    public void delete(Long id) {
        TaxCalculation taxCalculation = getEntity(id);
        propertyService.requirePropertyOwner(taxCalculation.getProperty());
        taxCalculationRepository.delete(taxCalculation);
    }

    public TaxCalculation getEntity(Long id) {
        return taxCalculationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax calculation not found"));
    }

    public void requireTaxCalculationOwner(TaxCalculation taxCalculation) {
        propertyService.requirePropertyOwner(taxCalculation.getProperty());
    }

    private TaxCalculationResponse toResponse(TaxCalculation taxCalculation) {
        List<PaymentResponse> payments = paymentRepository.findByTaxCalculationId(taxCalculation.getId())
            .stream()
            .map(paymentMapper::toResponse)
            .toList();

        return taxCalculationMapper.toResponse(taxCalculation, payments);
    }
}
