package com.ytk.ytkoj.domain.usr.service.internal;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.domain.UserRole;

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

    /***
     * 소셜 로그인 시에 나오는 유저 식별 키를 이용하여 유저 도메인을 찾습니다.
     * @param socialUqId 소셜 로그인 시에 나오는 유저 식별 키
     * @see User
     * @throws com.ytk.ytkoj.global.exception.NoResourceException 유저를 찾을 수 없을 때
     * @return User 도메인 객체
     */
    User findUserBySocialUqId(String socialUqId);

    /***
     * 유저 고유 uuid로 유저를 찾습니다.
     * @param userUuid 유저 uuid
     * @throws com.ytk.ytkoj.global.exception.NoResourceException 유저를 찾을 수 없을 때
     * @return User 도메인 객체
     */
    User findUserByUuid(String userUuid);

    /***
     * 새로운 유저를 등록합니다.
     * @param handle 유저 핸들
     * @param registerService 가입 서비스 (소셜 로그인이면 소셜 이름)
     * @param serviceUniqueId 가입 서비스의 유니크 아이디
     * @param userRole 등록되는 유저 권한
     * @see User
     * @return User 도메인 객체
     */
    User registerNewUser(String handle, String registerService, String serviceUniqueId, UserRole userRole);
}
