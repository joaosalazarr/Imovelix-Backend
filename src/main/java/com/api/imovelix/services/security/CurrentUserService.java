package com.api.imovelix.services.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.services.security.contracts.CurrentUserPort;

@Component
public class CurrentUserService implements CurrentUserPort {
    public AuthenticatedUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required");
        }

        return user;
    }

    public Long currentUserId() {
        return currentUser().userId();
    }

    public Long currentAuthenticationId() {
        return currentUser().authenticationId();
    }

    public void requireCurrentUser(Long userId) {
        if (!currentUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    public void requireCurrentAuthentication(Long authenticationId) {
        if (!currentAuthenticationId().equals(authenticationId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }
}
