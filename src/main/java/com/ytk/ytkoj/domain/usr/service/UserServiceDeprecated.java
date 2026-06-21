package com.ytk.ytkoj.domain.usr.service;

import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import com.ytk.ytkoj.global.authentication.CustomUserDetails;
import com.ytk.ytkoj.global.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 서비스 클래스는 기본적으로 DTO를 반환하지 않고 엔티티 혹은 인스턴스를 반환하도록 합니다.
 * */
@Service
@RequiredArgsConstructor // 생성자 주입 방식으로 운영
@CacheConfig(cacheNames = "user")
public class UserServiceDeprecated {
    private final UserJpaRepository userJpaRepository;
    private final UserQueryService userQueryService;

    /*
    CREATE
     * */

    /**
     * 유저는 반드시 소셜로그인 혹은 추후에 개발될 자체 서비스에 의해 가입이 되게 됩니다.
     * */
    public UserJpaEntity save(String handle, String registerService, String serviceUniqueId){
        UserJpaEntity userJpaEntity = new UserJpaEntity(handle, registerService, serviceUniqueId);
        return userJpaRepository.save(userJpaEntity);
    }

    /**
     * JWT 토큰 정보를 바탕으로 현재 로그인 된 사용자를 반환합니다.
     * */
    @Transactional
    public UserJpaEntity authenticateUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null
                || authentication.getPrincipal() == null
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) throw new UnAuthorizedException("Authentication Required");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userQueryService.findByUserUuid(userDetails.getUserUuid());
    }


    /**
     * 자체 서비스 로그인을 할 때 해당 함수를 사용합니다.
     * */
//    public User save(String username, String handle, String registerService){
//        return save(username, handle, registerService, null);
//    }


    /*
    READ
    * */




    /**
     * 소셜 로그인 시 기존 사용자 정보를 DB에서 찾기 위해 서비스 유니크 아이디를 통해서 사용자를 검색합니다.
     * */
    public Optional<UserJpaEntity> findByServiceUniqueId(String serviceUniqueId){
        return userJpaRepository.findByServiceUniqueId(serviceUniqueId);
    }

    public Optional<UserJpaEntity> findByHandle(String handle){
        return userJpaRepository.findByHandle(handle);
    }

    /*
    UPDATE
    * */

    @Transactional
    public UserJpaEntity updateUserHandle(String handle){
        UserJpaEntity userJpaEntity = authenticateUser();
        userJpaEntity.setHandle(handle);
        return userJpaRepository.save(userJpaEntity);
    }
}
