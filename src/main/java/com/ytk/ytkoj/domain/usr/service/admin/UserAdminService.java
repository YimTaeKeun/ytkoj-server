package com.ytk.ytkoj.domain.usr.service.admin;

import com.ytk.ytkoj.domain.usr.domain.User;

import java.util.List;

/***
 * 관리자에 의해 관리되는 유저 서비스 입니다.
 */
public interface UserAdminService {
    /*
    1. 유저 리스트 가져오기
    * */

    /***
     * 모든 사용자 정보를 가져옵니다.
     * @return 사용자 리스트
     */
    List<User> getAllUser();
}
