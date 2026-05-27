package com.api.imovelix.services.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.imovelix.repositories.UserAuthenticationRepository;
import com.api.imovelix.services.security.contracts.JwtServicePort;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHENTICATION_ID_ATTRIBUTE = "authenticatedAuthenticationId";
    public static final String USER_ID_ATTRIBUTE = "authenticatedUserId";

    private static final String API_PREFIX = "/api/v1/";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtServicePort jwtService;
    private final UserAuthenticationRepository userAuthenticationRepository;

    public JwtAuthenticationFilter(
        JwtServicePort jwtService,
        UserAuthenticationRepository userAuthenticationRepository
    ) {
        this.jwtService = jwtService;
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        if (!requiresAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractBearerToken(request);
        if (token == null) {
            writeUnauthorized(response, request, "Missing or invalid Authorization header");
            return;
        }

        try {
            if (!jwtService.isValid(token)) {
                writeUnauthorized(response, request, "Invalid or expired token");
                return;
            }

            Long authenticationId = jwtService.extractAuthenticationId(token);
            if (!userAuthenticationRepository.existsActiveById(authenticationId)) {
                writeUnauthorized(response, request, "Invalid or inactive token owner");
                return;
            }

            Long userId = jwtService.extractUserId(token);
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(authenticationId, userId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                java.util.List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute(AUTHENTICATION_ID_ATTRIBUTE, authenticationId);
            request.setAttribute(USER_ID_ATTRIBUTE, userId);
            filterChain.doFilter(request, response);
        } catch (RuntimeException exception) {
            SecurityContextHolder.clearContext();
            writeUnauthorized(response, request, "Invalid or expired token");
        }
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String path = applicationPath(request);
        String method = request.getMethod();

        return path.startsWith(API_PREFIX)
            && !"OPTIONS".equalsIgnoreCase(method)
            && !("POST".equalsIgnoreCase(method) && "/api/v1/auth/login".equals(path))
            && !("POST".equalsIgnoreCase(method) && "/api/v1/auth/mfa/verify".equals(path))
            && !("POST".equalsIgnoreCase(method) && "/api/v1/users".equals(path));
    }

    private String applicationPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isBlank() && path.startsWith(contextPath)) {
            return path.substring(contextPath.length());
        }

        return path;
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        return token.isBlank() ? null : token;
    }

    private void writeUnauthorized(
        HttpServletResponse response,
        HttpServletRequest request,
        String message
    ) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
            {"timestamp":"%s","status":%d,"error":"%s","message":"%s","path":"%s","fieldErrors":[]}
            """.formatted(
            escapeJson(LocalDateTime.now().toString()),
            HttpStatus.UNAUTHORIZED.value(),
            escapeJson(HttpStatus.UNAUTHORIZED.getReasonPhrase()),
            escapeJson(message),
            escapeJson(applicationPath(request))
        ));
    }

    private String escapeJson(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }
}
