package com.ytk.ytkoj.domain.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ytk.ytkoj.domain.auth.dto.AuthDTOs;
import com.ytk.ytkoj.domain.auth.dto.SocialUserInfoDTO;
import com.ytk.ytkoj.global.exception.BadRequestException;
import com.ytk.ytkoj.global.external_api_handler.KakaoApiHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoWebLoginRegister implements SocialLoginRegister{

    private final KakaoApiHandler kakaoApiHandler;

    @Override
    public SocialUserInfoDTO getSocialUserInfo(AuthDTOs.SocialLoginRequest request) {
        String authCode = request.token();
        String redirectUri = request.redirectUri();
        if(authCode == null || redirectUri == null) throw new BadRequestException("필수 정보 누락");

        // 카카오 API를 통하여 유저 정보를 가져옵니다.
        JsonNode response = kakaoApiHandler.requestTokenInfo(authCode, redirectUri);
        String accessToken = response.get("access_token").asText();
        JsonNode kakaoUserInfoResponse = kakaoApiHandler.requestUserInfo(accessToken);
        JsonNode kakaoAccount = kakaoUserInfoResponse.get("kakao_account");
        JsonNode profile = kakaoAccount.get("profile");
        String nickname = profile.get("nickname").asText();
        String id = kakaoUserInfoResponse.get("id").asText();
        return SocialUserInfoDTO
                .builder()
                .serviceUniqueId(id)
                .username(nickname)
                .build();
    }
}
