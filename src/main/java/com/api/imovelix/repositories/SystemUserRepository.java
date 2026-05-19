package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.SystemUser;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    Optional<SystemUser> findByCpf(String cpf);

    Boolean existsByCpf(String cpf);
}
