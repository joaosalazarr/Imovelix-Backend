package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.TaxCalculation;

import java.util.List;

@Repository
public interface TaxCalculationRepository extends JpaRepository<TaxCalculation, Long>  {
    List<TaxCalculation> findByPropertyId(Long propertyId);

    List<TaxCalculation> findByTaxId(Long taxId);
}
