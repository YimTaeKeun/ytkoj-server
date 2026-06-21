package com.ytk.ytkoj.domain.usr.service;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.dto.UserCommandDTO;

public interface UserService {
    /**
     * 로그인 된 유저를 가져옵니다.
     * */
    User getAuthenticatedUser();

    /**
     * 유저를 서비스에 가입 시킵니다.
     * 즉, DB에 유저 정보를 저장합니다.
     * */
    User registerUser(UserCommandDTO dto);

    /**
     * 유저의 핸들 정보를 업데이트 합니다.
     * */
    User updateUserHandle(String handle);

    /**
     * 소셜 로그인을 통해 나온 유저 식별 키를 이용해 서비스 가입 여부를 판단합니다.
     * */
    User findByServiceUniqueId(String serviceUniqueId);

    /**
     * 고유 닉네임으로 유저를 찾습니다.
     * */
    User findByHandle(String handle);

    /**
     * 서비스에서 사용하는 고유 유저 식별 uuid로 회원을 찾습니다.
     * */
    User findByUserUuid(String uuid);

}
