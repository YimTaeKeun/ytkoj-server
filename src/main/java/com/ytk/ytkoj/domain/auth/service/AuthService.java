package com.ytk.ytkoj.domain.auth.service;

import com.ytk.ytkoj.domain.auth.dto.AuthDTOs;
import com.ytk.ytkoj.domain.auth.dto.SocialUserInfoDTO;
import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.repository.UserRepository;
import com.ytk.ytkoj.domain.usr.service.UserService;
import com.ytk.ytkoj.global.exception.NoResourceException;
import com.ytk.ytkoj.global.token.TokenManager;
import com.ytk.ytkoj.global.token.TokenType;
import com.ytk.ytkoj.global.token.blacklist.BlackListToken;
import com.ytk.ytkoj.global.token.blacklist.BlackListTokenRepository;
import io.jsonwebtoken.Claims;
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
    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final BlackListTokenRepository blackListTokenRepository;


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
        String[] tokens = tokenManager.generateTokens(user);
        String accessToken = tokens[0], refreshToken = tokens[1];
        return new AuthDTOs.TokenResponse(
                accessToken,
                refreshToken
        );
    }

    public User register(SocialUserInfoDTO socialUserInfo, String service){
        return userService.save(
                socialUserInfo.getUsername(),
                null,
                service,
                socialUserInfo.getServiceUniqueId()
        );
    }

    public void logout(String accessToken, String refreshToken){
        // 토큰들을 모두 블랙리스트에 저장시킵니다.
        Claims accessTokenClaims = tokenManager.validateToken(accessToken, TokenType.ACCESS);
        Claims refreshTokenClaims = tokenManager.validateToken(refreshToken, TokenType.REFRESH);
        BlackListToken ac = new BlackListToken(accessTokenClaims.getId());
        BlackListToken rf = new BlackListToken(refreshTokenClaims.getId());
        blackListTokenRepository.save(ac);
        blackListTokenRepository.save(rf);
    }

    public AuthDTOs.TokenResponse refreshToken(String refreshToken){
        // refresh 토큰은 새로 발급하지 않고 액세스 토큰만 새로 발급합니다.
        Claims claims = tokenManager.validateToken(refreshToken, TokenType.REFRESH);

        String handle = claims.get("handle").toString();

        User user = userRepository.findByHandle(handle).orElseThrow(() -> new NoResourceException("유효하지 않은 사용자"));

        String accessToken = tokenManager.generateAccessToken(user);
        return new AuthDTOs.TokenResponse(accessToken, refreshToken);
    }

    public SocialLoginRegister getSocialService(String service){
        return socialService.get(service + "LoginRegister");
    }
}
