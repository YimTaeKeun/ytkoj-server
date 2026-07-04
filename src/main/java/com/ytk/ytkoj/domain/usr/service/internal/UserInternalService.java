package com.ytk.ytkoj.domain.usr.service.internal;

import com.ytk.ytkoj.domain.usr.domain.User;

/***
 * 타 애플리케이션과 통신을 위한 서비스입니다.
 */
public interface UserInternalService {
    /***
     * 액세스 토큰에 기반하여 현재 로그인 중인 사용자를 가져옵니다.
     * @see User
     * @return User 도메인 객체
     */
    User getAuthenticateUser();
}
