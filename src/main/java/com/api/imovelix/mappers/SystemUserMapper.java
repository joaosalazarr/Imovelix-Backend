package com.api.imovelix.mappers;

import org.springframework.stereotype.Component;

import com.api.imovelix.dto.request.RegisterSystemUserRequest;
import com.api.imovelix.dto.request.UpdateSystemUserRequest;
import com.api.imovelix.dto.response.SystemUserResponse;
import com.api.imovelix.models.SystemUser;

@Component
public class SystemUserMapper {
    public SystemUser toEntity(RegisterSystemUserRequest request) {
        return SystemUser.builder()
            .name(request.name())
            .cpf(request.cpf())
            .phoneNumber(request.phoneNumber())
            .active(true)
            .build();
    }

    public void updateEntity(UpdateSystemUserRequest request, SystemUser systemUser) {
        if (request.name() != null) {
            systemUser.setName(request.name());
        }

        if (request.cpf() != null) {
            systemUser.setCpf(request.cpf());
        }

        if (request.phoneNumber() != null) {
            systemUser.setPhoneNumber(request.phoneNumber());
        }

        if (request.active() != null) {
            systemUser.setActive(request.active());
        }
    }

    public SystemUserResponse toResponse(SystemUser systemUser) {
        return new SystemUserResponse(
            systemUser.getId(),
            systemUser.getName(),
            systemUser.getCpf(),
            systemUser.getPhoneNumber(),
            systemUser.getAuth() == null ? null : systemUser.getAuth().getEmail(),
            systemUser.getActive()
        );
    }

    public SystemUser toSystemUser(RegisterSystemUserRequest request) {
        return toEntity(request);
    }

    public SystemUserResponse toSystemUserResponse(SystemUser systemUser) {
        return toResponse(systemUser);
    }
}
