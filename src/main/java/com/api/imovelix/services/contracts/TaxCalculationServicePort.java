package com.api.imovelix.services.contracts;

import java.util.List;

import com.api.imovelix.dto.request.RegisterTaxCalculationRequest;
import com.api.imovelix.dto.response.TaxCalculationResponse;
import com.api.imovelix.dto.response.TaxCalculationSummaryResponse;
import com.api.imovelix.models.TaxCalculation;

public interface TaxCalculationServicePort {
    TaxCalculationResponse create(RegisterTaxCalculationRequest request);

    List<TaxCalculationSummaryResponse> findByProperty(Long propertyId);

    TaxCalculationResponse findById(Long id);

    void delete(Long id);

    TaxCalculation getEntity(Long id);

    void requireTaxCalculationOwner(TaxCalculation taxCalculation);
}
