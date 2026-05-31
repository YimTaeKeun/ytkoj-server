package com.ytk.ytkoj.domain.usr.entity;

import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String registerService; // 가입된 서비스

    @Setter
    @Column(unique = true)
    private String handle; // 서비스에서 사용하는 고유 닉네임

    @Column
    private String serviceUniqueId; // 가입된 서비스에서 나온 사용자 고유 아이디

    // UUID 중복 확률은 사실상 0에 가까워 무시할 수 있는 수준이다.
    @Column(unique = true)
    private String userUuid; // 회원을 찾기 위한 유저 아이디

    public User(String handle, String userUuid, String registerService, String serviceUniqueId) {
        this.userUuid = userUuid;
        this.handle = handle;
        this.registerService = registerService;
        this.serviceUniqueId = serviceUniqueId;
    }

    public User(String handle, String registerService, String serviceUniqueId) {
        this(handle, UUID.randomUUID().toString(), registerService, serviceUniqueId);
    }
}
