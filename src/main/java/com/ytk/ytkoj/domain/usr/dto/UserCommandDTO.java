package com.ytk.ytkoj.domain.usr.dto;

public record UserCommandDTO(
        String registerService,
        String handle,
        String serviceUqId
) {
}
