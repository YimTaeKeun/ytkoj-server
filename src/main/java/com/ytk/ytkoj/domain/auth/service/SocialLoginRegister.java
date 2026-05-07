package com.ytk.ytkoj.domain.auth.service;

import com.ytk.ytkoj.domain.auth.dto.AuthDTOs;
import com.ytk.ytkoj.domain.auth.dto.SocialUserInfoDTO;

/**
 * 해당 인터페이스를 상속받는 클래스는 반드시 클래스명이 ...LoginRegister로 끝나야 합니다.
 * LoginRegister로 이름이 끝나야 AuthService에서 Bean을 찾아서 연결해줄 수 있습니다.
 * */
public interface SocialLoginRegister {
    SocialUserInfoDTO getSocialUserInfo(AuthDTOs.SocialLoginRequest request);
}
