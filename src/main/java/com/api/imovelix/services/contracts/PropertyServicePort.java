package com.api.imovelix.services.contracts;

import java.util.List;

import com.api.imovelix.dto.request.ListPropertiesRequest;
import com.api.imovelix.dto.request.RegisterPropertyRequest;
import com.api.imovelix.dto.request.UpdatePropertyRequest;
import com.api.imovelix.dto.response.PageResponse;
import com.api.imovelix.dto.response.PropertyDetailsResponse;
import com.api.imovelix.dto.response.PropertyResponse;
import com.api.imovelix.models.Property;

public interface PropertyServicePort {
    PropertyResponse create(RegisterPropertyRequest request);

    PageResponse<PropertyResponse> findAll(ListPropertiesRequest request);

    List<PropertyResponse> findByUser(Long userId);

    PropertyDetailsResponse findDetailsById(Long id);

    PropertyResponse update(Long id, UpdatePropertyRequest request);

    void delete(Long id);

    Property getEntity(Long id);

    void requirePropertyOwner(Property property);
}
