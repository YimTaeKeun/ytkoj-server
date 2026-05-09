package com.ytk.ytkoj.domain.usr.service;

import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.repository.UserRepository;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 서비스 클래스는 기본적으로 DTO를 반환하지 않고 엔티티 혹은 인스턴스를 반환하도록 합니다.
 * */
@Service
@RequiredArgsConstructor // 생성자 주입 방식으로 운영
public class UserService {
    private final UserRepository userRepository;

    /*
    CREATE
     * */

    /**
     * 유저는 반드시 소셜로그인 혹은 추후에 개발될 자체 서비스에 의해 가입이 되게 됩니다.
     * */
    public User save(String username, String handle, String registerService, String serviceUniqueId){
        User user = new User(username, handle, registerService, serviceUniqueId);
        return userRepository.save(user);
    }

    /**
     * JWT 토큰 정보를 바탕으로 현재 로그인 된 사용자를 반환합니다.
     * */
    @Transactional
    public User authenticateUser(){
        // TODO: 로그인된 사용자를 반환해야합니다. 코드 교체 필요
        User user = userRepository.findByHandle("testUser").orElse(new User("test", "testUser", "kakaoWeb", "testId"));
        return userRepository.save(user);
    }


    /**
     * 자체 서비스 로그인을 할 때 해당 함수를 사용합니다.
     * */
    public User save(String username, String handle, String registerService){
        return save(username, handle, registerService, null);
    }


    /*
    READ
    * */


    /**
     * 서비스에서 자체 부여한 UUID를 통해서 사용자를 검색합니다.
     * */
    public User findByUserUuid(String UUID){
        return userRepository.findByUserUuid(UUID).orElseThrow(() -> new NoResourceException("해당 사용자가 없습니다."));
    }

    /**
     * 소셜 로그인 시 기존 사용자 정보를 DB에서 찾기 위해 서비스 유니크 아이디를 통해서 사용자를 검색합니다.
     * */
    public Optional<User> findByServiceUniqueId(String serviceUniqueId){
        return userRepository.findByServiceUniqueId(serviceUniqueId);
    }
}
