package com.ytk.ytkoj.domain.auth.service;

import com.ytk.ytkoj.domain.auth.dto.AuthDTOs;
import com.ytk.ytkoj.domain.auth.dto.SocialUserInfoDTO;
import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.domain.UserRole;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import com.ytk.ytkoj.domain.usr.service.internal.UserInternalService;
import com.ytk.ytkoj.global.exception.NoResourceException;
import com.ytk.ytkoj.global.token.TokenManager;
import com.ytk.ytkoj.global.token.TokenType;
import com.ytk.ytkoj.global.token.blacklist.BlackListToken;
import com.ytk.ytkoj.global.token.blacklist.BlackListTokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 로그인/회원가입을 담당하는 클래스 입니다.
 * */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final Map<String, SocialLoginRegister> socialService;
    private final UserInternalService userService;
    private final TokenManager tokenManager;
    private final BlackListTokenService blackListTokenService;


    /**
     * 로그인 혹은 회원가입을 진행합니다.
     * */
    public AuthDTOs.TokenResponse loginRegister(AuthDTOs.SocialLoginRequest request){
        SocialLoginRegister socialService = getSocialService(request.service());
        // 소셜 로그인을 이용하여 사용자 정보를 가져옵니다.
        SocialUserInfoDTO socialUserInfo = socialService.getSocialUserInfo(request);
        // 사용자 정보를 바탕으로 DB 조회를 진행하여 사용자가 없으면 회원가입을 진행합니다.
        // 사용자가 있다면 DB에 있는 유저를 반환합니다.
        User user;
        try{
            user = userService.findUserBySocialUqId(socialUserInfo.getServiceUniqueId());
        }catch (Exception e){
            log.warn("exception: {}", e.getMessage());
            user = register(socialUserInfo, request.service());
        }
        // 유저 정보를 바탕으로 JWT 토큰을 반환합니다.
        String[] tokens = tokenManager.generateTokens(user);
        String accessToken = tokens[0], refreshToken = tokens[1];
        return new AuthDTOs.TokenResponse(
                accessToken,
                refreshToken
        );
    }

    public User register(SocialUserInfoDTO socialUserInfo, String service){
        return userService.registerNewUser(
                null, // 핸들은 처음 가입시 미지정
                service,
                socialUserInfo.getServiceUniqueId(),
                UserRole.CLIENT // 유저 기본 권한은 일반 고객으로 강제합니다.
        );
    }

    public void logout(String accessToken, String refreshToken){
        // 토큰들을 모두 블랙리스트에 저장시킵니다.
        Claims accessTokenClaims = tokenManager.validateToken(accessToken, TokenType.ACCESS);
        Claims refreshTokenClaims = tokenManager.validateToken(refreshToken, TokenType.REFRESH);
        BlackListToken ac = new BlackListToken(accessTokenClaims.getId());
        BlackListToken rf = new BlackListToken(refreshTokenClaims.getId());
        blackListTokenService.save(ac);
        blackListTokenService.save(rf);
    }

    public AuthDTOs.TokenResponse refreshToken(String refreshToken){
        // refresh 토큰은 새로 발급하지 않고 액세스 토큰만 새로 발급합니다.
        Claims claims = tokenManager.validateToken(refreshToken, TokenType.REFRESH);

        // 유저 uuid를 이용하여 유저를 가져옴
        String uuid = claims.getSubject();
        User user = userService.findUserByUuid(uuid);
        String accessToken = tokenManager.generateAccessToken(user);
        return new AuthDTOs.TokenResponse(accessToken, refreshToken);
    }

    public SocialLoginRegister getSocialService(String service){
        return socialService.get(service + "LoginRegister");
    }
}
