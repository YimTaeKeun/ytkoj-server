package com.ytk.ytkoj.domain.usr.dto;

import com.ytk.ytkoj.domain.usr.domain.UserRole;

import java.time.LocalDateTime;

public class AdmResDTOs {

    public record UserDTO(
            String userUuid,
            String registerService,
            String handle,
            UserRole userRole,
            String registeredAt
    ){}
}
