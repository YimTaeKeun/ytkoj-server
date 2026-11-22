package com.ytk.ytkoj.domain.usr.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
//    UUID로 사용자를 찾습니다.
    Optional<UserJpaEntity> findByUserUuid(String userUuid);
//    소셜 로그인 아이디로 사용자를 찾습니다.
    Optional<UserJpaEntity> findByServiceUniqueId(String serviceUniqueId);

    Optional<UserJpaEntity> findByHandle(String handle);
}
