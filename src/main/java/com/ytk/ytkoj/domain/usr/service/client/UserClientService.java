package com.ytk.ytkoj.domain.usr.service.client;

import com.ytk.ytkoj.domain.usr.domain.User;

/***
 * 프레젠테이션 레이어에서 사용하기 위한 서비스 입니다.
 */
public interface UserClientService {
    /***
     * 액세스 토큰에 기반하여 현재 로그인 중인 사용자를 가져옵니다.
     * @see User
     * @return User 도메인 객체
     */
    User getAuthenticateUser();

    /***
     * 유저 handle이 서비스 내에서 사용 가능한지 검사합니다.
     * @param targetHandle 검사할 핸들
     * @return true: 등록 가능, false: 등록 불가
     */
    Boolean isAvailableHandle(String targetHandle);

    /***
     *
     * @param handle 등록 위한 handle
     * @see User
     * @return User 도메인 객체
     */
    User uploadHandle(String handle);
}
