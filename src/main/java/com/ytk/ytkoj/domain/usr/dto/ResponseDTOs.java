package com.ytk.ytkoj.domain.usr.dto;

public class ResponseDTOs {
    public record UserResponse(
            Boolean hasHandle,
            String handle,
            String userUuid,
            String registerService
    ){}
}
