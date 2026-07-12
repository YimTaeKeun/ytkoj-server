package com.ytk.ytkoj.global.token.blacklist;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 캐싱을 위해 클래스 분리
 * */
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = "black")
public class BlackListTokenService {

    private final BlackListTokenRepository blackListTokenRepository;

    @Cacheable(value = "black", key="#jti")
    public BlackListToken findByJti(String jti){
        return blackListTokenRepository.findByJti(jti).orElse(null);
    }

    @CacheEvict(value = "black", key = "#blackListToken.jti")
    public void save(BlackListToken blackListToken){
        blackListTokenRepository.save(blackListToken);
    }

}
