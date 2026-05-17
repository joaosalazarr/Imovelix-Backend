package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.TaxCalculation;

@Repository
public interface TaxCalculationRepository extends JpaRepository<TaxCalculation, Long>  {
    
}
