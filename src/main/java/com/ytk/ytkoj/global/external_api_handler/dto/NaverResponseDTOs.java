package com.ytk.ytkoj.global.external_api_handler.dto;

public class NaverResponseDTOs {
    public record TokenInfo(
            String accessToken
    ){}

    public record UserInfo(
            String resultCode,
            String message,
            String id,
            String nickname
    ){}
}
