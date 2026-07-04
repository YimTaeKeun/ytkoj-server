package com.ytk.ytkoj.domain.usr.service.internal;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.domain.UserRole;
import com.ytk.ytkoj.domain.usr.infra.UserRepository;
import com.ytk.ytkoj.global.authentication.CustomUserDetails;
import com.ytk.ytkoj.global.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInternalServiceImpl implements UserInternalService{

    private final UserRepository userRepository;

    @Override
    public User getAuthenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null
                || authentication.getPrincipal() == null
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) throw new UnAuthorizedException("Authentication Required");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public User findUserBySocialUqId(String socialUqId) {
        return userRepository.findBySocialUqId(socialUqId);
    }

    @Override
    public User registerNewUser(String handle, String registerService, String serviceUniqueId, UserRole userRole) {
        // 도메인 내에서 사용하는 객체 고유 키
        String userServicePk = UUID.randomUUID().toString();

        User build = User.builder()
                .userUuid(userServicePk)
                .registerService(registerService)
                .handle(handle)
                .socialUQId(serviceUniqueId)
                .userRole(userRole)
                .build();

        return userRepository.registerNewUser(build);
    }

    @Override
    public User findUserByUuid(String userUuid) {
        return userRepository.findByUserUuid(userUuid);
    }
}
