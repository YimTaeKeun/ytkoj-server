package com.ytk.ytkoj.global.authentication;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.UserRepository;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "userDetails")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    /**
     * 고유 handle을 통해 유저를 특정합니다.
     * */
    @Cacheable(value = "userDetails", key = "#userUuid")
    @Override
    public UserDetails loadUserByUsername(String userUuid) throws UsernameNotFoundException {
        User user = userRepository.findByUserUuid(userUuid);
        return new CustomUserDetails(
                user
        );
    }
}
