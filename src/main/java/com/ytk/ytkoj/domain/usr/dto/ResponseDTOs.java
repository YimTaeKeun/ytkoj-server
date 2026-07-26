package com.ytk.ytkoj.domain.usr.dto;

import com.ytk.ytkoj.domain.usr.domain.UserRole;

public class ResponseDTOs {
    public record UserResponse(
            Boolean hasHandle,
            String handle,
            String userUuid,
            UserRole userRole,
            String registerService
    ){}
}
