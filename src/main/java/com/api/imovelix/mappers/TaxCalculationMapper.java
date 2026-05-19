package com.api.imovelix.mappers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.api.imovelix.dto.request.RegisterTaxCalculationRequest;
import com.api.imovelix.dto.response.PaymentResponse;
import com.api.imovelix.dto.response.TaxCalculationResponse;
import com.api.imovelix.dto.response.TaxCalculationSummaryResponse;
import com.api.imovelix.models.Property;
import com.api.imovelix.models.Tax;
import com.api.imovelix.models.TaxCalculation;

@Component
public class TaxCalculationMapper {
    private final PropertyMapper propertyMapper;
    private final TaxMapper taxMapper;

    public TaxCalculationMapper(PropertyMapper propertyMapper, TaxMapper taxMapper) {
        this.propertyMapper = propertyMapper;
        this.taxMapper = taxMapper;
    }

    public TaxCalculation toEntity(RegisterTaxCalculationRequest request, Property property, Tax tax) {
        BigDecimal taxValue = request.baseValue()
            .multiply(request.appliedPercentage())
            .divide(BigDecimal.valueOf(100));

        return TaxCalculation.builder()
            .property(property)
            .tax(tax)
            .refferenceYear(request.refferenceYear())
            .baseValue(toLong(request.baseValue()))
            .appliedPercentage(toLong(request.appliedPercentage()))
            .taxValue(toLong(taxValue))
            .build();
    }

    public TaxCalculationResponse toResponse(
        TaxCalculation taxCalculation,
        List<PaymentResponse> payments
    ) {
        return new TaxCalculationResponse(
            taxCalculation.getId(),
            taxCalculation.getRefferenceYear(),
            toBigDecimal(taxCalculation.getBaseValue()),
            toBigDecimal(taxCalculation.getAppliedPercentage()),
            toBigDecimal(taxCalculation.getTaxValue()),
            taxCalculation.getCalculatedAt(),
            taxMapper.toSummaryResponse(taxCalculation.getTax()),
            propertyMapper.toSummaryResponse(taxCalculation.getProperty()),
            payments
        );
    }

    public TaxCalculationSummaryResponse toSummaryResponse(TaxCalculation taxCalculation) {
        return new TaxCalculationSummaryResponse(
            taxCalculation.getId(),
            taxCalculation.getRefferenceYear(),
            toBigDecimal(taxCalculation.getTaxValue()),
            taxCalculation.getCalculatedAt()
        );
    }

    private BigDecimal toBigDecimal(Long value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    private Long toLong(BigDecimal value) {
        return value == null ? null : value.longValue();
    }
}
