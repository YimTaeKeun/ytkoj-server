package com.ytk.ytkoj.global.token.blacklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Long> {
    Optional<BlackListToken> findByJti(String jti);
}
