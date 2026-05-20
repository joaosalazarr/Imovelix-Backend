package com.api.imovelix.services.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.api.imovelix.repositories.UserAuthenticationRepository;

import jakarta.servlet.ServletException;

class JwtAuthenticationFilterTest {
    private final StubJwtService jwtService = new StubJwtService();
    private final StubUserAuthenticationRepository userAuthenticationRepository = new StubUserAuthenticationRepository();
    private final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userAuthenticationRepository.proxy());

    @Test
    void allowsLoginWithoutToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(jwtService.validatedTokens).isEmpty();
    }

    @Test
    void blocksProtectedEndpointWithoutBearerToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/properties");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).contains("Missing or invalid Authorization header");
    }

    @Test
    void allowsProtectedEndpointWithValidTokenFromActiveUser() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/properties");
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        jwtService.validTokens.put("valid-token", true);
        jwtService.authenticationIds.put("valid-token", 10L);
        jwtService.userIds.put("valid-token", 20L);
        userAuthenticationRepository.activeAuthenticationIds.put(10L, true);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(request.getAttribute(JwtAuthenticationFilter.AUTHENTICATION_ID_ATTRIBUTE)).isEqualTo(10L);
        assertThat(request.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE)).isEqualTo(20L);
    }

    @Test
    void blocksProtectedEndpointWhenTokenOwnerIsInactive() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/properties");
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtService.validTokens.put("valid-token", true);
        jwtService.authenticationIds.put("valid-token", 10L);
        userAuthenticationRepository.activeAuthenticationIds.put(10L, false);

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).contains("Invalid or inactive token owner");
    }

    private static class StubJwtService extends JwtService {
        private final Map<String, Boolean> validTokens = new HashMap<>();
        private final Map<String, Long> authenticationIds = new HashMap<>();
        private final Map<String, Long> userIds = new HashMap<>();
        private final Map<String, Boolean> validatedTokens = new HashMap<>();

        StubJwtService() {
            super("test-secret-change-me", 3600);
        }

        @Override
        public boolean isValid(String token) {
            validatedTokens.put(token, true);
            return validTokens.getOrDefault(token, false);
        }

        @Override
        public Long extractAuthenticationId(String token) {
            return authenticationIds.get(token);
        }

        @Override
        public Long extractUserId(String token) {
            return userIds.get(token);
        }
    }

    private static class StubUserAuthenticationRepository {
        private final Map<Long, Boolean> activeAuthenticationIds = new HashMap<>();

        private UserAuthenticationRepository proxy() {
            return (UserAuthenticationRepository) Proxy.newProxyInstance(
                UserAuthenticationRepository.class.getClassLoader(),
                new Class<?>[] { UserAuthenticationRepository.class },
                (proxy, method, args) -> {
                    if ("existsActiveById".equals(method.getName())) {
                        return activeAuthenticationIds.getOrDefault((Long) args[0], false);
                    }
                    throw new UnsupportedOperationException(method.getName());
                }
            );
        }
    }
}
