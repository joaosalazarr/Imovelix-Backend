package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.Tax;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long>  {
    
}
