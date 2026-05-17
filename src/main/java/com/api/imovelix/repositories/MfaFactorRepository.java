package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.MfaFactor;

@Repository
public interface MfaFactorRepository extends JpaRepository<MfaFactor, Long>  {
    
}
