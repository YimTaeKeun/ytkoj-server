package com.ytk.ytkoj.domain.auth.service;


import com.ytk.ytkoj.domain.auth.dto.AuthDTOs;
import com.ytk.ytkoj.domain.auth.dto.SocialUserInfoDTO;
import com.ytk.ytkoj.global.external_api_handler.NaverApiHandler;
import com.ytk.ytkoj.global.external_api_handler.dto.NaverResponseDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverWebLoginRegister implements SocialLoginRegister{
    private final NaverApiHandler naverApiHandler;


    @Override
    public SocialUserInfoDTO getSocialUserInfo(AuthDTOs.SocialLoginRequest request) {
        String accessToken = naverApiHandler.requestTokenInfo(request.token()).accessToken();
        NaverResponseDTOs.UserInfo userInfo = naverApiHandler.requestUserInfo(accessToken);
        return SocialUserInfoDTO.builder()
                .serviceUniqueId(userInfo.id())
                .build();
    }
}
