package com.evo.ddd.application.mapper;

import com.evo.ddd.application.dto.request.*;
import com.evo.ddd.domain.command.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    CreateOrUpdatePermissionCmd from(CreateOrUpdatePermissionRequest request);
    LoginCmd from(LoginRequest request);
    VerifyOtpCmd from(VerifyOtpRequest request);
    CreateUserCmd from(CreateUserRequest request);
    ChangePasswordCmd from(ChangePasswordRequest request);
    CreateOrUpdateRoleCmd from(CreateOrUpdateRoleRequest request);
    DeleteRolePermissionCmd from(DeleteRolePermissionRequest request);
    CreateRolePermissionCmd from(CreateRolePermissionRequest request);
    CreateUserRoleCmd from(CreateUserRoleRequest request);
    UpdateUserCmd from(UpdateUserRequest request);
    WriteLogCmd from(String activity);
}
