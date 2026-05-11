package com.ytk.ytkoj.global.authentication;

import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.repository.UserRepository;
import com.ytk.ytkoj.domain.usr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    /**
     * 고유 handle을 통해 유저를 특정합니다.
     * */
    @Override
    public UserDetails loadUserByUsername(String userUuid) throws UsernameNotFoundException {
        User user = userRepository.findByUserUuid(userUuid).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(
                user.getHandle(),
                user.getUsername(),
                user.getUserUuid()
        );
    }
}
