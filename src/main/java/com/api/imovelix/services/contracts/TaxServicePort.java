package com.api.imovelix.services.contracts;

import java.util.List;

import com.api.imovelix.dto.request.RegisterTaxRequest;
import com.api.imovelix.dto.request.UpdateTaxRequest;
import com.api.imovelix.dto.response.TaxDetailsResponse;
import com.api.imovelix.dto.response.TaxResponse;
import com.api.imovelix.models.Tax;

public interface TaxServicePort {
    TaxResponse create(RegisterTaxRequest request);

    List<TaxResponse> findAll();

    TaxDetailsResponse findDetailsById(Long id);

    TaxResponse update(Long id, UpdateTaxRequest request);

    void delete(Long id);

    Tax getEntity(Long id);
}
