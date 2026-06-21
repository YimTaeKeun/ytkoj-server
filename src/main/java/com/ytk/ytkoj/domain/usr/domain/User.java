package com.ytk.ytkoj.domain.usr.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public class User {
    // db pk를 직접 사용합니다.

    // 회원 가입 방식
    String registerService;

    // 서비스에서 사용하는 고유 닉네임
    @Setter
    String handle;

    // 소셜 소비스 로그인 시 사용하는 유니크 아이디
    String serviceUqId;

    // 유저를 구분하기 위한 고유 uuid
    String userUuid;

    @Builder
    public User(
            String registerService,
            String handle,
            String serviceUqId,
            @NonNull
            String userUuid
    ){
        this.registerService = registerService;
        this.handle = handle;
        this.serviceUqId = serviceUqId;
        this.userUuid = userUuid;
    }

}
