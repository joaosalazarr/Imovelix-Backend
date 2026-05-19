package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.Payment;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>  {
    List<Payment> findByTaxCalculationId(Long taxCalculationId);
}
