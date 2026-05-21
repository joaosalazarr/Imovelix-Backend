package com.api.imovelix.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.dto.request.RegisterPaymentRequest;
import com.api.imovelix.dto.request.UpdatePaymentRequest;
import com.api.imovelix.dto.request.UpdatePaymentStatusRequest;
import com.api.imovelix.dto.response.PaymentResponse;
import com.api.imovelix.mappers.PaymentMapper;
import com.api.imovelix.models.Payment;
import com.api.imovelix.models.TaxCalculation;
import com.api.imovelix.repositories.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final TaxCalculationService taxCalculationService;
    private final PaymentMapper paymentMapper;

    public PaymentService(
        PaymentRepository paymentRepository,
        TaxCalculationService taxCalculationService,
        PaymentMapper paymentMapper
    ) {
        this.paymentRepository = paymentRepository;
        this.taxCalculationService = taxCalculationService;
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    public PaymentResponse create(RegisterPaymentRequest request) {
        TaxCalculation taxCalculation = taxCalculationService.getEntity(request.taxCalculationId());
        taxCalculationService.requireTaxCalculationOwner(taxCalculation);
        Payment payment = paymentMapper.toEntity(request, taxCalculation);
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> findByTaxCalculation(Long taxCalculationId) {
        taxCalculationService.requireTaxCalculationOwner(taxCalculationService.getEntity(taxCalculationId));
        return paymentRepository.findByTaxCalculationId(taxCalculationId)
            .stream()
            .map(paymentMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse findById(Long id) {
        Payment payment = getEntity(id);
        taxCalculationService.requireTaxCalculationOwner(payment.getTaxCalculation());
        return paymentMapper.toResponse(payment);
    }

    @Transactional
    public PaymentResponse update(Long id, UpdatePaymentRequest request) {
        Payment payment = getEntity(id);
        taxCalculationService.requireTaxCalculationOwner(payment.getTaxCalculation());
        paymentMapper.updateEntity(request, payment);
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse updateStatus(Long id, UpdatePaymentStatusRequest request) {
        Payment payment = getEntity(id);
        taxCalculationService.requireTaxCalculationOwner(payment.getTaxCalculation());
        paymentMapper.updateStatus(request, payment);
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public void delete(Long id) {
        Payment payment = getEntity(id);
        taxCalculationService.requireTaxCalculationOwner(payment.getTaxCalculation());
        paymentRepository.delete(payment);
    }

    public Payment getEntity(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }
}
