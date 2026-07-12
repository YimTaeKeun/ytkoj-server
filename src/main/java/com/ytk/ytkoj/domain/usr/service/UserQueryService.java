package com.ytk.ytkoj.domain.usr.service;

import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.repository.UserRepository;
import com.ytk.ytkoj.global.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Caching을 위해 리포지토리에서 유저를 가져오는 서비스는 해당 클래스에서 처리합니다.
 * Caching은 AOP로 작동하는데 같은 클래스에서 함수를 호출하면 Caching 전략이 적용되지 않기 때문
 * */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserQueryService {
    private final UserRepository userRepository;

    /**
     * 서비스에서 자체 부여한 UUID를 통해서 사용자를 검색합니다.
     * */
    @Cacheable(value = "user", key = "#UUID")
    public User findByUserUuid(String UUID){
        return userRepository.findByUserUuid(UUID).orElseThrow(() -> new UnAuthorizedException("Authentication Required"));
    }
}
