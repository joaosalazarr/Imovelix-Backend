package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>  {
    
}
