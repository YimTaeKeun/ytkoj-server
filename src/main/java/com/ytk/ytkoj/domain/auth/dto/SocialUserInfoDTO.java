package com.ytk.ytkoj.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 해당 클래스는 소셜 로그인 서버로부터 날라오는 유저 정보를 정형화 하기 위한 클래스입니다.
 * */
@Builder
@Getter
public class SocialUserInfoDTO {
    private String username;
    private String serviceUniqueId;
}
