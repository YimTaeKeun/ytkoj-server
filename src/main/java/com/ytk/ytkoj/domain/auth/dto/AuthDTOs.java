package com.ytk.ytkoj.domain.auth.dto;

public class AuthDTOs {
    public record SocialLoginRequest(
            String service,
            String token,
            String redirectUri
    ){}

    public record TokenResponse(
            String accessToken,
            String refreshToken
    ){}

    public record LogoutRequest(
            String accessToken,
            String refreshToken
    ){}

    public record RefreshRequest(
            String refreshToken
    ){}
}
