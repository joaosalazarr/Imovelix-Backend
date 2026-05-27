package com.api.imovelix.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.imovelix.dto.request.RegisterPaymentRequest;
import com.api.imovelix.dto.request.UpdatePaymentRequest;
import com.api.imovelix.dto.request.UpdatePaymentStatusRequest;
import com.api.imovelix.dto.response.PaymentResponse;
import com.api.imovelix.services.contracts.PaymentServicePort;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentServicePort paymentService;

    public PaymentController(PaymentServicePort paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody RegisterPaymentRequest request) {
        return paymentService.create(request);
    }

    @GetMapping("/by-tax-calculation/{taxCalculationId}")
    public List<PaymentResponse> findByTaxCalculation(@PathVariable @Positive Long taxCalculationId) {
        return paymentService.findByTaxCalculation(taxCalculationId);
    }

    @GetMapping("/{id}")
    public PaymentResponse findById(@PathVariable @Positive Long id) {
        return paymentService.findById(id);
    }

    @PutMapping("/{id}")
    public PaymentResponse update(@PathVariable @Positive Long id, @Valid @RequestBody UpdatePaymentRequest request) {
        return paymentService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public PaymentResponse updateStatus(
        @PathVariable @Positive Long id,
        @Valid @RequestBody UpdatePaymentStatusRequest request
    ) {
        return paymentService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        paymentService.delete(id);
    }
}
