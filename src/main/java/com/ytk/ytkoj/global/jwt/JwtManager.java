package com.ytk.ytkoj.global.jwt;

import com.ytk.ytkoj.global.config.JwtConfig;
import com.ytk.ytkoj.global.exception.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtManager {
    private final JwtConfig conf;
    private final JwtVerifier jwtVerifier = new JwtVerifier();
    /*
    JWT 토큰은 헤더, 바디, 사인으로 이루어져 있으며, 헤더와 바디, 시크릿 키를 이용하여 사인을 구성한다.
    * */

    /**
     * JWT 토큰 유효성 검증
     * */
    public Claims verifyJwt(String jwt){
        return jwtVerifier.verifyJwt(jwt);
    }

    /**
     * JWT 토큰 발급
     * */
    public String generateJwt(Claims claims){
        claims.setIssuer(conf.getIssuer()); // 토큰 발급자 설정
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(conf.getSecretKey().getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .setClaims(claims)
                // setHeader는 기존 헤더를 제거하는 거지만 setHeaderParams는 키를 추가하는 방식
                .setHeaderParams(Map.of(
                        "typ", conf.getTyp(),
                        "alg", conf.getAlg()
                ))
                .compact();
    }

    private class JwtVerifier{

        /**
         * JWT를 검증합니다. 토큰이 변조되지 않았음을 검증합니다.
         * @throws UnAuthorizedException 토큰 검증 실패시 발생하는 예외입니다.
         * @param jwt 검증할 jwt 토큰을 말합니다.
         * */
        public Claims verifyJwt(String jwt) {
            // 1. 서명 검증
            Claims claims = verifySignature(jwt);
            // 2. 내부 값 검증
            verifyPayload(claims);
            return claims;
        }

        private Claims verifySignature(String jwt) {
            // 서명, 만료시간, 발행시간 자동 검증
            SecretKey secretKey = Keys.hmacShaKeyFor(conf.getSecretKey().getBytes(StandardCharsets.UTF_8));
            Claims claims;
            try{
                claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt).getBody();
            } catch (JwtException e){
                throw new UnAuthorizedException(e.getMessage());
            }
            return claims;
        }

        private void verifyPayload(Claims claims){
            try{
                // iss 검증
                if(!claims.getIssuer().equals(conf.getIssuer())) throw new UnAuthorizedException("Invalid");

            }catch (Exception e){
                throw new UnAuthorizedException("Invalid payload");
            }
        }
    }
}
