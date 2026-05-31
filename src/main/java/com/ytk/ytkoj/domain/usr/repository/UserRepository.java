package com.ytk.ytkoj.domain.usr.repository;

import com.ytk.ytkoj.domain.usr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    UUID로 사용자를 찾습니다.
    Optional<User> findByUserUuid(String userUuid);
//    소셜 로그인 아이디로 사용자를 찾습니다.
    Optional<User> findByServiceUniqueId(String serviceUniqueId);

    Optional<User> findByHandle(String handle);
}
