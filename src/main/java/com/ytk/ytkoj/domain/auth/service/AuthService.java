package com.ytk.ytkoj.domain.auth.service;

import com.ytk.ytkoj.domain.auth.dto.AuthDTOs;
import com.ytk.ytkoj.domain.auth.dto.SocialUserInfoDTO;
import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 로그인/회원가입을 담당하는 클래스 입니다.
 * */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final Map<String, SocialLoginRegister> socialService;
    private final UserService userService;


    /**
     * 로그인 혹은 회원가입을 진행합니다.
     * */
    public AuthDTOs.TokenResponse loginRegister(AuthDTOs.SocialLoginRequest request){
        SocialLoginRegister socialService = getSocialService(request.service());
        // 소셜 로그인을 이용하여 사용자 정보를 가져옵니다.
        SocialUserInfoDTO socialUserInfo = socialService.getSocialUserInfo(request);
        // 사용자 정보를 바탕으로 DB 조회를 진행하여 사용자가 없으면 회원가입을 진행합니다.
        // 사용자가 있다면 DB에 있는 유저를 반환합니다.
        User user = userService.findByServiceUniqueId(socialUserInfo.getServiceUniqueId()).orElseGet(
                () -> register(socialUserInfo, request.service())
        );
        // 유저 정보를 바탕으로 JWT 토큰을 반환합니다.
        return new AuthDTOs.TokenResponse(
                "None",
                "None"
        );
    }

    public User register(SocialUserInfoDTO socialUserInfo, String service){
        return userService.save(
                socialUserInfo.getUsername(),
                service,
                socialUserInfo.getServiceUniqueId()
        );
    }

    public SocialLoginRegister getSocialService(String service){
        return socialService.get(service + "LoginRegister");
    }
}
