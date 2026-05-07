package com.ytk.ytkoj.global.token;

import org.springframework.stereotype.Component;

/**
 * 유저 정보를 바탕으로 서비스 액세스 토큰과 리프레시 토큰을 생성/삭제 및 관리하는 역할을 합니다.
 * JWT 토큰의 body에는 다음과 같은 정보가 포함됩니다.
 * 유저 UUID, 유저 이름
 * */
@Component
public class TokenManager {

    public String[] generateTokens(){
        return new String[2];
    }

    public String generateAccessToken(){
        // 액세스 토큰을 생성합니다.
        return "";
    }

    public String generateRefreshToken(){
        return "";
    }


}
