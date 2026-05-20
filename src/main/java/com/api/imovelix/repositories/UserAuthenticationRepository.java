package com.api.imovelix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.imovelix.models.UserAuthentication;

import java.util.Optional;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long>  {
    Optional<UserAuthentication> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("""
        select count(authentication) > 0
        from UserAuthentication authentication
        join authentication.user user
        where authentication.id = :id
            and user.active = true
        """)
    boolean existsActiveById(@Param("id") Long id);
}
