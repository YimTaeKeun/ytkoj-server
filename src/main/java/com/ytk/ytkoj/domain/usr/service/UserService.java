package com.ytk.ytkoj.domain.usr.service;

import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * 서비스 클래스는 기본적으로 DTO를 반환하지 않고 엔티티 혹은 인스턴스를 반환하도록 합니다.
 * */
@Service
@RequiredArgsConstructor // 생성자 주입 방식으로 운영
public class UserService {
    private final UserRepository userRepository;

    public User findByUserUuid(String UUID){
        return userRepository.findByUserUuid(UUID).orElse(null);
    }
}
