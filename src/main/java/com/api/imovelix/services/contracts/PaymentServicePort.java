package com.api.imovelix.services.contracts;

import java.util.List;

import com.api.imovelix.dto.request.RegisterPaymentRequest;
import com.api.imovelix.dto.request.UpdatePaymentRequest;
import com.api.imovelix.dto.request.UpdatePaymentStatusRequest;
import com.api.imovelix.dto.response.PaymentResponse;
import com.api.imovelix.models.Payment;

public interface PaymentServicePort {
    PaymentResponse create(RegisterPaymentRequest request);

    List<PaymentResponse> findByTaxCalculation(Long taxCalculationId);

    PaymentResponse findById(Long id);

    PaymentResponse update(Long id, UpdatePaymentRequest request);

    PaymentResponse updateStatus(Long id, UpdatePaymentStatusRequest request);

    void delete(Long id);

    Payment getEntity(Long id);
}
