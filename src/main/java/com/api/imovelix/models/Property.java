package com.api.imovelix.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.api.imovelix.enums.PropertyPourpose;
import com.api.imovelix.enums.PropertyType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "properties")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false, length = 50)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "pourpose")
    private PropertyPourpose pourpose;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(name = "street", nullable = false, length = 150)
    private String street;

    @Column(name = "street_number", nullable = false, length = 20)
    private Integer streetNumber;

    @Column(name = "complement", length = 100)
    private String complement;

    @Column(name = "neighborhood", nullable = false, length = 100)
    private String neighborhood;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "state", nullable = false, length = 2)
    private String state;

    @Column(
        name = "total_area", 
        nullable = false, 
        precision = 10, 
        scale = 2
    )
    private BigDecimal totalArea;

    @Column(
        name = "built_area", 
        nullable = false,
        precision = 10,
        scale = 2
    )
    private Long built_area;

    @Column(
        name = "sale_value", 
        nullable = false,
        precision = 12,
        scale = 2
    )
    private Long saleValue;

    @Column(
        name = "purchase_value", 
        nullable = false,
        precision = 10,
        scale = 2
    )
    private Long purchaseValue;

    @Column(name = "acquisition_date")
    private LocalDateTime acquisitionDate;

    @Column(name = "has_financing", nullable = false)
    private Boolean hasFinancing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private SystemUser systemUser;

    @OneToMany(
        mappedBy = "property",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<TaxCalculation> taxesCalculations = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (hasFinancing == null) {
            hasFinancing = false;
        }
    }
}
