package com.api.imovelix.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.api.imovelix.dto.request.RegisterTaxRequest;
import com.api.imovelix.dto.request.UpdateTaxRequest;
import com.api.imovelix.dto.response.TaxCalculationSummaryResponse;
import com.api.imovelix.dto.response.TaxDetailsResponse;
import com.api.imovelix.dto.response.TaxResponse;
import com.api.imovelix.dto.response.TaxSummaryResponse;
import com.api.imovelix.models.Tax;

@Component
public class TaxMapper {
    public Tax toEntity(RegisterTaxRequest request) {
        return Tax.builder()
            .name(request.name())
            .description(request.description())
            .percentualBase(request.percentualBase())
            .periodicity(request.periodicity())
            .build();
    }

    public void updateEntity(UpdateTaxRequest request, Tax tax) {
        if (request.name() != null) {
            tax.setName(request.name());
        }
        if (request.description() != null) {
            tax.setDescription(request.description());
        }
        if (request.percentualBase() != null) {
            tax.setPercentualBase(request.percentualBase());
        }
        if (request.periodicity() != null) {
            tax.setPeriodicity(request.periodicity());
        }
    }

    public TaxResponse toResponse(Tax tax) {
        return new TaxResponse(
            tax.getId(),
            tax.getName(),
            tax.getDescription(),
            tax.getPercentualBase(),
            tax.getPeriodicity()
        );
    }

    public TaxSummaryResponse toSummaryResponse(Tax tax) {
        return new TaxSummaryResponse(
            tax.getId(),
            tax.getName(),
            tax.getPercentualBase(),
            tax.getPeriodicity()
        );
    }

    public TaxDetailsResponse toDetailsResponse(
        Tax tax,
        List<TaxCalculationSummaryResponse> taxCalculations
    ) {
        return new TaxDetailsResponse(
            tax.getId(),
            tax.getName(),
            tax.getDescription(),
            tax.getPercentualBase(),
            tax.getPeriodicity(),
            taxCalculations
        );
    }
}
