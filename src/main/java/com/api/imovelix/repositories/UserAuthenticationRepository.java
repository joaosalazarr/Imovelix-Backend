package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.UserAuthentication;

import java.util.Optional;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long>  {
    Optional<UserAuthentication> findByEmail(String email);

    Boolean existsByEmail(String email);
}
