package com.ytk.ytkoj.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtConfig {
    private String secretKey;

    // 토큰 발급자
    private String issuer;

    // 알고리즘 유형
    private String alg = "HS256";

    // 토큰 타입
    private String typ = "JWT";

    // 액세스 토큰 유효기간
    private Long accessTokenValidationPeriod;

    // 리프레시 토큰 유효기간
    private Long refreshTokenValidationPeriod;
}
