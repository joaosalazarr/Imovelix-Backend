package com.api.imovelix.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.dto.request.ListPropertiesRequest;
import com.api.imovelix.dto.request.RegisterPropertyRequest;
import com.api.imovelix.dto.request.UpdatePropertyRequest;
import com.api.imovelix.dto.response.PageResponse;
import com.api.imovelix.dto.response.PropertyDetailsResponse;
import com.api.imovelix.dto.response.PropertyResponse;
import com.api.imovelix.mappers.PropertyMapper;
import com.api.imovelix.mappers.TaxCalculationMapper;
import com.api.imovelix.models.Property;
import com.api.imovelix.models.SystemUser;
import com.api.imovelix.repositories.PropertyRepository;
import com.api.imovelix.repositories.TaxCalculationRepository;
import com.api.imovelix.services.contracts.PropertyServicePort;
import com.api.imovelix.services.contracts.SystemUserServicePort;
import com.api.imovelix.services.security.contracts.CurrentUserPort;

@Service
public class PropertyService implements PropertyServicePort {
    private static final Set<String> SORTABLE_FIELDS = Set.of(
        "id",
        "propertyType",
        "pourpose",
        "city",
        "state",
        "saleValue",
        "purchaseValue",
        "totalArea",
        "acquisitionDate"
    );

    private final PropertyRepository propertyRepository;
    private final TaxCalculationRepository taxCalculationRepository;
    private final SystemUserServicePort systemUserService;
    private final PropertyMapper propertyMapper;
    private final TaxCalculationMapper taxCalculationMapper;
    private final CurrentUserPort currentUserService;

    public PropertyService(
        PropertyRepository propertyRepository,
        TaxCalculationRepository taxCalculationRepository,
        SystemUserServicePort systemUserService,
        PropertyMapper propertyMapper,
        TaxCalculationMapper taxCalculationMapper,
        CurrentUserPort currentUserService
    ) {
        this.propertyRepository = propertyRepository;
        this.taxCalculationRepository = taxCalculationRepository;
        this.systemUserService = systemUserService;
        this.propertyMapper = propertyMapper;
        this.taxCalculationMapper = taxCalculationMapper;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public PropertyResponse create(RegisterPropertyRequest request) {
        currentUserService.requireCurrentUser(request.userId());
        SystemUser systemUser = systemUserService.getEntity(request.userId());
        Property property = propertyMapper.toEntity(request, systemUser);
        return propertyMapper.toResponse(propertyRepository.save(property));
    }

    @Transactional(readOnly = true)
    public PageResponse<PropertyResponse> findAll(ListPropertiesRequest request) {
        Long authenticatedUserId = currentUserService.currentUserId();
        if (request.userId() != null && !authenticatedUserId.equals(request.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        Pageable pageable = PageRequest.of(
            request.pageOrDefault(),
            request.sizeOrDefault(),
            Sort.by(sortDirection(request), sortField(request))
        );

        return PageResponse.from(propertyRepository.findAll(specification(request, authenticatedUserId), pageable).map(propertyMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> findByUser(Long userId) {
        currentUserService.requireCurrentUser(userId);
        return propertyRepository.findBySystemUserId(userId)
            .stream()
            .map(propertyMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public PropertyDetailsResponse findDetailsById(Long id) {
        Property property = getEntity(id);
        requirePropertyOwner(property);
        return propertyMapper.toDetailsResponse(
            property,
            taxCalculationRepository.findByPropertyId(id)
                .stream()
                .map(taxCalculationMapper::toSummaryResponse)
                .toList()
        );
    }

    @Transactional
    public PropertyResponse update(Long id, UpdatePropertyRequest request) {
        Property property = getEntity(id);
        requirePropertyOwner(property);
        propertyMapper.updateEntity(request, property);
        return propertyMapper.toResponse(propertyRepository.save(property));
    }

    @Transactional
    public void delete(Long id) {
        Property property = getEntity(id);
        requirePropertyOwner(property);
        propertyRepository.delete(property);
    }

    public Property getEntity(Long id) {
        return propertyRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
    }

    private Sort.Direction sortDirection(ListPropertiesRequest request) {
        return "desc".equalsIgnoreCase(request.sortDirectionOrDefault()) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    private String sortField(ListPropertiesRequest request) {
        String sortBy = request.sortByOrDefault();
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid property sort field");
        }
        return sortBy;
    }

    public void requirePropertyOwner(Property property) {
        currentUserService.requireCurrentUser(property.getSystemUser().getId());
    }

    private Specification<Property> specification(ListPropertiesRequest request, Long authenticatedUserId) {
        return Specification
            .where(equalsValue("systemUser.id", authenticatedUserId))
            .and(equalsValue("propertyType", request.propertyType()))
            .and(equalsValue("pourpose", request.pourpose()))
            .and(likeIgnoreCase("city", request.city()))
            .and(likeIgnoreCase("state", request.state()))
            .and(equalsValue("hasFinancing", request.hasFinancing()))
            .and(greaterThanOrEqualTo("saleValue", toLong(request.minSaleValue())))
            .and(lessThanOrEqualTo("saleValue", toLong(request.maxSaleValue())))
            .and(greaterThanOrEqualTo("totalArea", request.minTotalArea()))
            .and(lessThanOrEqualTo("totalArea", request.maxTotalArea()));
    }

    private Specification<Property> equalsValue(String fieldPath, Object value) {
        return (root, query, builder) -> value == null ? null : builder.equal(resolve(root, fieldPath), value);
    }

    private Specification<Property> likeIgnoreCase(String fieldPath, String value) {
        return (root, query, builder) -> value == null || value.isBlank()
            ? null
            : builder.like(builder.lower(resolve(root, fieldPath)), "%" + value.toLowerCase() + "%");
    }

    private <T extends Comparable<? super T>> Specification<Property> greaterThanOrEqualTo(String fieldPath, T value) {
        return (root, query, builder) -> value == null ? null : builder.greaterThanOrEqualTo(resolve(root, fieldPath), value);
    }

    private <T extends Comparable<? super T>> Specification<Property> lessThanOrEqualTo(String fieldPath, T value) {
        return (root, query, builder) -> value == null ? null : builder.lessThanOrEqualTo(resolve(root, fieldPath), value);
    }

    @SuppressWarnings("unchecked")
    private <T> jakarta.persistence.criteria.Path<T> resolve(jakarta.persistence.criteria.Path<?> root, String fieldPath) {
        jakarta.persistence.criteria.Path<?> path = root;
        for (String part : fieldPath.split("\\.")) {
            path = path.get(part);
        }
        return (jakarta.persistence.criteria.Path<T>) path;
    }

    private Long toLong(BigDecimal value) {
        return value == null ? null : value.longValue();
    }
}
