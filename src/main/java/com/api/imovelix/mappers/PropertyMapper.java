package com.api.imovelix.mappers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.api.imovelix.dto.request.RegisterPropertyRequest;
import com.api.imovelix.dto.request.UpdatePropertyRequest;
import com.api.imovelix.dto.response.PropertyDetailsResponse;
import com.api.imovelix.dto.response.PropertyResponse;
import com.api.imovelix.dto.response.PropertySummaryResponse;
import com.api.imovelix.dto.response.TaxCalculationSummaryResponse;
import com.api.imovelix.models.Property;
import com.api.imovelix.models.SystemUser;

@Component
public class PropertyMapper {
    public Property toEntity(RegisterPropertyRequest request, SystemUser systemUser) {
        return Property.builder()
            .systemUser(systemUser)
            .propertyType(request.propertyType())
            .pourpose(request.pourpose())
            .zipCode(request.zipCode())
            .street(request.street())
            .streetNumber(request.streetNumber())
            .complement(request.complement())
            .neighborhood(request.neighborhood())
            .city(request.city())
            .state(request.state())
            .totalArea(request.totalArea())
            .built_area(toLong(request.builtArea()))
            .saleValue(toLong(request.saleValue()))
            .purchaseValue(toLong(request.purchaseValue()))
            .acquisitionDate(request.acquisitionDate())
            .hasFinancing(request.hasFinancing())
            .build();
    }

    public void updateEntity(UpdatePropertyRequest request, Property property) {
        if (request.propertyType() != null) {
            property.setPropertyType(request.propertyType());
        }
        if (request.pourpose() != null) {
            property.setPourpose(request.pourpose());
        }
        if (request.zipCode() != null) {
            property.setZipCode(request.zipCode());
        }
        if (request.street() != null) {
            property.setStreet(request.street());
        }
        if (request.streetNumber() != null) {
            property.setStreetNumber(request.streetNumber());
        }
        if (request.complement() != null) {
            property.setComplement(request.complement());
        }
        if (request.neighborhood() != null) {
            property.setNeighborhood(request.neighborhood());
        }
        if (request.city() != null) {
            property.setCity(request.city());
        }
        if (request.state() != null) {
            property.setState(request.state());
        }
        if (request.totalArea() != null) {
            property.setTotalArea(request.totalArea());
        }
        if (request.builtArea() != null) {
            property.setBuilt_area(toLong(request.builtArea()));
        }
        if (request.saleValue() != null) {
            property.setSaleValue(toLong(request.saleValue()));
        }
        if (request.purchaseValue() != null) {
            property.setPurchaseValue(toLong(request.purchaseValue()));
        }
        if (request.acquisitionDate() != null) {
            property.setAcquisitionDate(request.acquisitionDate());
        }
        if (request.hasFinancing() != null) {
            property.setHasFinancing(request.hasFinancing());
        }
    }

    public PropertyResponse toResponse(Property property) {
        return new PropertyResponse(
            property.getId(),
            property.getPropertyType(),
            property.getPourpose(),
            property.getZipCode(),
            property.getStreet(),
            property.getStreetNumber(),
            property.getComplement(),
            property.getNeighborhood(),
            property.getCity(),
            property.getState(),
            property.getTotalArea(),
            toBigDecimal(property.getBuilt_area()),
            toBigDecimal(property.getSaleValue()),
            toBigDecimal(property.getPurchaseValue()),
            property.getAcquisitionDate(),
            property.getHasFinancing()
        );
    }

    public PropertySummaryResponse toSummaryResponse(Property property) {
        return new PropertySummaryResponse(
            property.getId(),
            property.getStreet(),
            property.getStreetNumber(),
            property.getNeighborhood(),
            property.getCity(),
            property.getState()
        );
    }

    public PropertyDetailsResponse toDetailsResponse(
        Property property,
        List<TaxCalculationSummaryResponse> taxCalculations
    ) {
        return new PropertyDetailsResponse(
            property.getId(),
            property.getPropertyType(),
            property.getPourpose(),
            property.getZipCode(),
            property.getStreet(),
            property.getStreetNumber(),
            property.getComplement(),
            property.getNeighborhood(),
            property.getCity(),
            property.getState(),
            property.getTotalArea(),
            toBigDecimal(property.getBuilt_area()),
            toBigDecimal(property.getSaleValue()),
            toBigDecimal(property.getPurchaseValue()),
            property.getAcquisitionDate(),
            property.getHasFinancing(),
            taxCalculations
        );
    }

    private BigDecimal toBigDecimal(Long value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    private Long toLong(BigDecimal value) {
        return value == null ? null : value.longValue();
    }
}
