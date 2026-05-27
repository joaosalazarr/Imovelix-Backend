package com.api.imovelix.services.contracts;

import java.util.List;

import com.api.imovelix.dto.request.RegisterSystemUserRequest;
import com.api.imovelix.dto.request.UpdateSystemUserRequest;
import com.api.imovelix.dto.response.SystemUserResponse;
import com.api.imovelix.models.SystemUser;

public interface SystemUserServicePort {
    SystemUserResponse create(RegisterSystemUserRequest request);

    List<SystemUserResponse> findAll();

    SystemUserResponse findById(Long id);

    SystemUserResponse update(Long id, UpdateSystemUserRequest request);

    void deactivate(Long id);

    SystemUser getEntity(Long id);
}
