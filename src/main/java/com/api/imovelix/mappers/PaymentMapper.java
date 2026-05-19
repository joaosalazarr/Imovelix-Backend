package com.api.imovelix.mappers;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.api.imovelix.dto.request.RegisterPaymentRequest;
import com.api.imovelix.dto.request.UpdatePaymentRequest;
import com.api.imovelix.dto.request.UpdatePaymentStatusRequest;
import com.api.imovelix.dto.response.PaymentResponse;
import com.api.imovelix.models.Payment;
import com.api.imovelix.models.TaxCalculation;

@Component
public class PaymentMapper {
    public Payment toEntity(RegisterPaymentRequest request, TaxCalculation taxCalculation) {
        Payment payment = new Payment();
        payment.setTaxCalculation(taxCalculation);
        payment.setDueDate(request.dueDate());
        payment.setPaidAmount(toLong(request.paidAmount()));
        return payment;
    }

    public void updateEntity(UpdatePaymentRequest request, Payment payment) {
        if (request.dueDate() != null) {
            payment.setDueDate(request.dueDate());
        }
        if (request.paymentDate() != null) {
            payment.setPaymentDate(request.paymentDate());
        }
        if (request.paidAmount() != null) {
            payment.setPaidAmount(toLong(request.paidAmount()));
        }
        if (request.status() != null) {
            payment.setStatus(request.status());
        }
    }

    public void updateStatus(UpdatePaymentStatusRequest request, Payment payment) {
        payment.setStatus(request.status());
        if (request.paymentDate() != null) {
            payment.setPaymentDate(request.paymentDate());
        }
    }

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getDueDate(),
            payment.getPaymentDate(),
            toBigDecimal(payment.getPaidAmount()),
            payment.getStatus()
        );
    }

    private BigDecimal toBigDecimal(Long value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    private Long toLong(BigDecimal value) {
        return value == null ? null : value.longValue();
    }
}
