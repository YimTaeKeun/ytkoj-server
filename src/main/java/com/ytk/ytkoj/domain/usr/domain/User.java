package com.ytk.ytkoj.domain.usr.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class User {
    // 유저 식별키 (db pk와 다름)
    private final String userUuid;

    // 사용자 가입 서비스
    private final String registerService;

    // 사용자 가입 날짜
    private LocalDateTime registeredAt;

    // 유저 닉네임
    @Setter
    private String handle;

    // 소셜 로그인 과정에서 나온 유저 식별키
    private final String socialUQId;


    // 유저 권한
    private final UserRole userRole;

    public Boolean isAdmin(){
        return userRole == UserRole.ADMIN;
    }

    public Boolean hasHandle(){
        return handle != null;
    }

    /***
     * 서비스 이용이 가능한가
     * @return true: 서비스 이용가능, false: 서비스 이용불가
     */
    public Boolean serviceAvailable(){
        // TODO 서비스 접근 불가 여부 판독 시스템 개발
        return true;
    }


    @Builder
    public User(
            @NonNull
            String userUuid,
            String registerService,
            String handle,
            String socialUQId,
            @NonNull
            UserRole userRole,
            LocalDateTime registeredAt
    ){
        this.userUuid = userUuid;
        this.registerService = registerService;
        this.handle = handle;
        this.socialUQId = socialUQId;
        this.userRole = userRole;
        this.registeredAt = registeredAt;
    }
}
