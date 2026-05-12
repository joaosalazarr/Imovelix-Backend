package com.api.imovelix.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.api.imovelix.enums.TaxPeriodicity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "taxes")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(
        name = "base_percentage", 
        nullable = false,
        precision = 5,
        scale = 2
    )
    private BigDecimal percentualBase;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodicity", nullable = false, length = 30)
    private TaxPeriodicity periodicity;

    @OneToMany(mappedBy = "tax")
    @Builder.Default
    private List<TaxCalculation> taxesCalculations = new ArrayList<>();
}
