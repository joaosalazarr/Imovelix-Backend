package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.MfaFactor;

import java.util.List;

@Repository
public interface MfaFactorRepository extends JpaRepository<MfaFactor, Long>  {
    List<MfaFactor> findByUserAuthenticationId(Long userAuthenticationId);

    List<MfaFactor> findByUserAuthenticationIdAndActiveTrue(Long userAuthenticationId);
}
