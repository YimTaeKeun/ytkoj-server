package com.ytk.ytkoj.global.token;

import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.global.config.JwtConfig;
import com.ytk.ytkoj.global.exception.UnAuthorizedException;
import com.ytk.ytkoj.global.jwt.JwtManager;
import com.ytk.ytkoj.global.token.blacklist.BlackListToken;
import com.ytk.ytkoj.global.token.blacklist.BlackListTokenRepository;
import com.ytk.ytkoj.global.token.blacklist.BlackListTokenService;
import com.ytk.ytkoj.global.token.refresh.RefreshToken;
import com.ytk.ytkoj.global.token.refresh.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * 유저 정보를 바탕으로 서비스 액세스 토큰과 리프레시 토큰을 생성/삭제 및 관리하는 역할을 합니다.
 * JWT 토큰의 body에는 다음과 같은 정보가 포함됩니다.
 * 유저 UUID, 유저 이름
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenManager {
    private final JwtManager jwtManager;
    private final JwtConfig jwtConfig;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListTokenService blackListTokenService;

    /**
     * 값으로 반환되는 첫 번째 필드가 액세스 토큰, 두 번째 필드가 리프레시 토큰입니다.
     * */
    @Transactional
    public String[] generateTokens(User user){
        String access = generateAccessToken(user);
        String refresh = generateRefreshToken(user);
        return new String[] {access, refresh};
    }

    public String generateAccessToken(User user){
        // 액세스 토큰을 생성합니다.
        Claims claims = getClaims(user, TokenType.ACCESS);
        return jwtManager.generateJwt(claims);

    }

    /**
     * 1. 기본 JWT 서명 검증
     * </br>
     * 2. 유저 handle 검증
     * </br>
     * 3. 블랙리스트 토큰이 아닌지 검증
     * */
    public Claims validateToken(String token, TokenType type){
        // 토큰 유효성 검증
        Claims claims = jwtManager.verifyJwt(token);

        // 추가 토큰 페이로드 검증
        // handle 검증 제거
//        checkAdditionalPayload(claims);

        // 블랙리스트 토큰인지 체크
        String jti = claims.getId();
        if(jti == null) throw new UnAuthorizedException("INVALID");

        checkBlackListToken(jti);
        if(type == TokenType.REFRESH){
            refreshTokenRepository.findByJti(jti)
                    .orElseThrow(() -> new UnAuthorizedException("INVALID"));
        }
        return claims;
    }

    private void checkBlackListToken(String jti){
        BlackListToken blackListToken = blackListTokenService.findByJti(jti);
        if(blackListToken != null) throw new UnAuthorizedException("INVALID");
    }

//    private void checkAdditionalPayload(Claims claims){
//        // 유저 handle 검증
//        Object handle = claims.get("handle");
//        if(handle == null) throw new UnAuthorizedException("NO_HANDLE_USER");
//        log.info("handle: {}", handle);
//    }

    @Transactional
    public String generateRefreshToken(User user){
        // 리프레시 토큰 생성
        Claims claims = getClaims(user, TokenType.REFRESH);
        String refreshToken = jwtManager.generateJwt(claims);

        // 리프레시 토큰 저장
        refreshTokenRepository.save(
                new RefreshToken(refreshToken, claims.getId())
        );
        return refreshToken;
    }

    private Claims getClaims(User user, TokenType type){
        Claims claims = Jwts.claims();

        Date now = new Date();
        Date expTime;
        Long accessPeriod = jwtConfig.getAccessTokenValidationPeriod();
        Long refreshPeriod = jwtConfig.getRefreshTokenValidationPeriod();

        if(type == TokenType.ACCESS) expTime = new Date(now.getTime() + accessPeriod);
        else expTime = new Date(now.getTime() + refreshPeriod);

        // 토큰 만료 시간 설정
        claims.setExpiration(expTime);

        // jti 설정
        String jti = UUID.randomUUID().toString();
        claims.setId(jti);


        // 추가 클레임 설정
        claims.put("handle", user.getHandle()); // 유저 고유 핸들
//        claims.put("username", user.getUsername()); // 유저 닉네임
        claims.setSubject(user.getUserUuid()); // 유저 uuid

        return claims;
    }




}
