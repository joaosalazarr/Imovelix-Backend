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

@Service
public class TaxCalculationService {
    private final TaxCalculationRepository taxCalculationRepository;
    private final PaymentRepository paymentRepository;
    private final PropertyService propertyService;
    private final TaxService taxService;
    private final TaxCalculationMapper taxCalculationMapper;
    private final PaymentMapper paymentMapper;

    public TaxCalculationService(
        TaxCalculationRepository taxCalculationRepository,
        PaymentRepository paymentRepository,
        PropertyService propertyService,
        TaxService taxService,
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
        Tax tax = taxService.getEntity(request.taxId());
        TaxCalculation taxCalculation = taxCalculationRepository.save(taxCalculationMapper.toEntity(request, property, tax));

        return toResponse(taxCalculation);
    }

    @Transactional(readOnly = true)
    public List<TaxCalculationSummaryResponse> findByProperty(Long propertyId) {
        return taxCalculationRepository.findByPropertyId(propertyId)
            .stream()
            .map(taxCalculationMapper::toSummaryResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public TaxCalculationResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public void delete(Long id) {
        taxCalculationRepository.delete(getEntity(id));
    }

    public TaxCalculation getEntity(Long id) {
        return taxCalculationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax calculation not found"));
    }

    private TaxCalculationResponse toResponse(TaxCalculation taxCalculation) {
        List<PaymentResponse> payments = paymentRepository.findByTaxCalculationId(taxCalculation.getId())
            .stream()
            .map(paymentMapper::toResponse)
            .toList();

        return taxCalculationMapper.toResponse(taxCalculation, payments);
    }
}
