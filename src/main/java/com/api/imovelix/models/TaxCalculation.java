package com.api.imovelix.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "taxes_calculations")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class TaxCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refference_year", nullable = false)
    private Integer refferenceYear;

    @Column(
        name = "base_value", 
        nullable = false, 
        precision = 12, 
        scale = 2
    )
    private Long baseValue;

    @Column(
        name = "applied_percentage", 
        nullable = false, 
        precision = 5, 
        scale = 2
    )
    private Long appliedPercentage;

    @Column(
        name = "tax_value", 
        nullable = false,
        precision = 12,
        scale = 2
    )
    private Long taxValue;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id", nullable = false)
    private Tax tax;

    @OneToMany(
        mappedBy = "taxCalculation",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (calculatedAt == null) {
            calculatedAt = LocalDateTime.now();
        }
    }
}
