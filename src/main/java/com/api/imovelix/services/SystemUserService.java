package com.api.imovelix.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.dto.request.RegisterSystemUserRequest;
import com.api.imovelix.dto.request.UpdateSystemUserRequest;
import com.api.imovelix.dto.response.SystemUserResponse;
import com.api.imovelix.mappers.SystemUserMapper;
import com.api.imovelix.mappers.UserAuthenticationMapper;
import com.api.imovelix.models.SystemUser;
import com.api.imovelix.models.UserAuthentication;
import com.api.imovelix.repositories.SystemUserRepository;
import com.api.imovelix.repositories.UserAuthenticationRepository;
import com.api.imovelix.services.contracts.SystemUserServicePort;
import com.api.imovelix.services.security.contracts.CurrentUserPort;
import com.api.imovelix.services.security.contracts.PasswordHasherPort;

@Service
public class SystemUserService implements SystemUserServicePort {
    private final SystemUserRepository systemUserRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final SystemUserMapper systemUserMapper;
    private final UserAuthenticationMapper userAuthenticationMapper;
    private final PasswordHasherPort passwordHasher;
    private final CurrentUserPort currentUserService;

    public SystemUserService(
        SystemUserRepository systemUserRepository,
        UserAuthenticationRepository userAuthenticationRepository,
        SystemUserMapper systemUserMapper,
        UserAuthenticationMapper userAuthenticationMapper,
        PasswordHasherPort passwordHasher,
        CurrentUserPort currentUserService
    ) {
        this.systemUserRepository = systemUserRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.systemUserMapper = systemUserMapper;
        this.userAuthenticationMapper = userAuthenticationMapper;
        this.passwordHasher = passwordHasher;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public SystemUserResponse create(RegisterSystemUserRequest request) {
        ensureCpfIsAvailable(request.cpf(), null);
        ensureEmailIsAvailable(request.email(), null);

        SystemUser systemUser = systemUserMapper.toEntity(request);
        UserAuthentication authentication = userAuthenticationMapper.toEntity(
            request,
            passwordHasher.hash(request.password()),
            systemUser
        );
        systemUser.setAuth(authentication);

        return systemUserMapper.toResponse(systemUserRepository.save(systemUser));
    }

    @Transactional(readOnly = true)
    public List<SystemUserResponse> findAll() {
        return List.of(systemUserMapper.toResponse(getEntity(currentUserService.currentUserId())));
    }

    @Transactional(readOnly = true)
    public SystemUserResponse findById(Long id) {
        currentUserService.requireCurrentUser(id);
        return systemUserMapper.toResponse(getEntity(id));
    }

    @Transactional
    public SystemUserResponse update(Long id, UpdateSystemUserRequest request) {
        currentUserService.requireCurrentUser(id);
        SystemUser systemUser = getEntity(id);
        ensureCpfIsAvailable(request.cpf(), id);

        if (request.email() != null && systemUser.getAuth() != null) {
            ensureEmailIsAvailable(request.email(), systemUser.getAuth().getId());
            systemUser.getAuth().setEmail(request.email());
        }

        systemUserMapper.updateEntity(request, systemUser);
        return systemUserMapper.toResponse(systemUserRepository.save(systemUser));
    }

    @Transactional
    public void deactivate(Long id) {
        currentUserService.requireCurrentUser(id);
        SystemUser systemUser = getEntity(id);
        systemUser.setActive(false);
        systemUserRepository.save(systemUser);
    }

    public SystemUser getEntity(Long id) {
        return systemUserRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void ensureCpfIsAvailable(String cpf, Long currentUserId) {
        if (cpf == null) {
            return;
        }

        systemUserRepository.findByCpf(cpf)
            .filter(user -> !user.getId().equals(currentUserId))
            .ifPresent(user -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF already registered");
            });
    }

    private void ensureEmailIsAvailable(String email, Long currentAuthenticationId) {
        if (email == null) {
            return;
        }

        userAuthenticationRepository.findByEmail(email)
            .filter(authentication -> !authentication.getId().equals(currentAuthenticationId))
            .ifPresent(authentication -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail already registered");
            });
    }
}
