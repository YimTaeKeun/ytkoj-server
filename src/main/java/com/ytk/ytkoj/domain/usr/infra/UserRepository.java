package com.ytk.ytkoj.domain.usr.infra;

import com.ytk.ytkoj.domain.usr.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    /**
     * 유저 서비스 uuid로 회원을 찾습니다.
     * */
    User findByUserUuid(String userUuid);

    /**
     * 소셜 로그인 시 우리 서비스에 가입한 이력이 있는지 확인하기 위해 회원을 찾습니다.
     * 소셜 서비스를 통해 나온 유저 식별 아이디로 서비스 회원을 찾습니다.
     * */
    User findByServiceUniqueId(String serviceUniqueId);

    /**
     * 고유 유저 닉네임으로 찾습니다.
     * */
    User findByHandle(String handle);

    /**
     * 유저 정보를 저장하거나 업데이트 합니다.
     * */
    User save(User user);
}
