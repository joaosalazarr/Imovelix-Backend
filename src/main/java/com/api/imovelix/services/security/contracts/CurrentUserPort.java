package com.api.imovelix.services.security.contracts;

import com.api.imovelix.services.security.AuthenticatedUser;

public interface CurrentUserPort {
    AuthenticatedUser currentUser();

    Long currentUserId();

    Long currentAuthenticationId();

    void requireCurrentUser(Long userId);

    void requireCurrentAuthentication(Long authenticationId);
}
