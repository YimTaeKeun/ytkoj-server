package com.ytk.ytkoj.domain.usr.entity;

import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    // UUID 중복 확률은 사실상 0에 가까워 무시할 수 있는 수준이다.
    @Column(unique = true)
    private String userUuid; // 회원을 찾기 위한 유저 아이디

    public User(String username, String userUuid) {
        this.username = username;
        this.userUuid = userUuid;
    }

    public User(String username){
        this(username, UUID.randomUUID().toString());
    }
}
