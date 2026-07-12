package com.ytk.ytkoj.domain.usr.infra;

import com.ytk.ytkoj.domain.usr.domain.User;

public interface UserRepository {

    /***
     * user에 등록된 고유 닉네임 (Handle)을 바탕으로 유저를 탐색합니다.
     * @param handle 유저 고유 닉네임
     * @see User
     * @throws com.ytk.ytkoj.global.exception.NoResourceException 핸들을 가진 유저를 찾을 수 없을 때
     * @return User 도메인 반환
     */
    User findByHandle(String handle);

    /***
     * 소셜 로그인 시에 나오는 유저 식별 키로 유저를 특정합니다.
     * 소셜 로그인에 활용합니다.
     * @param socialUqId 소셜 로그인시 나오는 유저 식별 키
     * @see User
     * @throws com.ytk.ytkoj.global.exception.NoResourceException 유저를 찾을 수 없을 때
     * @return User 도메인 반환
     */
    User findBySocialUqId(String socialUqId);

    /***
     * 유저 uuid 즉, db pk가 아닌 도메인에서 사용하는 고유 유저 id로 유저를 탐색합니다.
     * @param uuid 유저 uuid
     * @see User
     * @throws com.ytk.ytkoj.global.exception.NoResourceException 유저를 찾을 수 없을 때
     * @return User 도메인 반환
     */
    User findByUserUuid(String uuid);

    /***
     * 유저 도메인을 바탕으로 신규 유저를 등록합니다.
     * @param user 유저 도메인
     * @see User
     * @throws com.ytk.ytkoj.global.exception.NoResourceException 유저를 찾을 수 없을 때
     * @return User 도메인 반환
     */
    User registerNewUser(User user);

    /***
     * 기존 유저를 업데이트 합니다.
     * @param user 유저 도메인
     * @see User
     * @throws com.ytk.ytkoj.global.exception.NoResourceException 유저를 찾을 수 없을 때
     * @return User 도메인 반환
     */
    User updateUser(User user);
}
