package com.ytk.ytkoj.domain.usr.infra.jpa;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.domain.UserRole;
import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "user")
@NoArgsConstructor
@Getter
public class UserJpaEntity extends BaseEntity {
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

    @Enumerated(EnumType.STRING)
    private UserRole userRole; // 유저 권한

    /***
     * 도메인을 엔티티로 변경합니다.
     * @param user 도메인 객체
     * @see User
     * @return jpa 엔티티 객체
     */
    public static UserJpaEntity toEntity(User user){
        return UserJpaEntity.builder()
                .handle(user.getHandle())
                .userUuid(user.getUserUuid())
                .registerService(user.getRegisterService())
                .serviceUniqueId(user.getSocialUQId())
                .userRole(user.getUserRole())
                .build();
    }

    /***
     * jpa 엔티티 객체를 User 도메인 객체로 변경합니다.
     * @see User
     * @return user 도메인 객체
     */
    public User toDomain(){
        return User.builder()
                .userUuid(this.userUuid)
                .registerService(this.registerService)
                .handle(this.handle)
                .socialUQId(this.serviceUniqueId)
                .userRole(this.userRole)
                .registeredAt(this.getCreatedAt())
                .build();
    }

    /***
     * 유저 정보를 변경합니다.
     * @param user 변경된 유저 정보
     * @return
     */
    public UserJpaEntity applyChange(User user){
        this.userUuid = user.getUserUuid();
        this.handle = user.getHandle();
        this.registerService = user.getRegisterService();
        this.serviceUniqueId = user.getSocialUQId();
        this.userRole = user.getUserRole();

        return this;
    }

    @Builder
    public UserJpaEntity(String handle, String userUuid, String registerService, String serviceUniqueId, UserRole userRole) {
        this.userUuid = userUuid;
        this.handle = handle;
        this.registerService = registerService;
        this.serviceUniqueId = serviceUniqueId;
        this.userRole = userRole;
    }

//    public UserJpaEntity(String handle, String registerService, String serviceUniqueId, UserRole userRole) {
//        this(handle, UUID.randomUUID().toString(), registerService, serviceUniqueId, userRole);
//    }
}
