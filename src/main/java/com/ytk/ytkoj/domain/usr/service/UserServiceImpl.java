package com.ytk.ytkoj.domain.usr.service;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.dto.UserCommandDTO;
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
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null
                || authentication.getPrincipal() == null
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) throw new UnAuthorizedException("Authentication Required");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findByUserUuid(userDetails.getUserUuid());
    }

    @Override
    public User registerUser(UserCommandDTO dto) {
        String newUserUuid = UUID.randomUUID().toString();
        User user = User.builder()
                .userUuid(newUserUuid)
                .serviceUqId(dto.serviceUqId())
                .handle(dto.handle())
                .registerService(dto.registerService())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUserHandle(String handle) {
        User authenticatedUser = getAuthenticatedUser();
        authenticatedUser.setHandle(handle);

        return userRepository.save(authenticatedUser);
    }

    @Override
    public User findByServiceUniqueId(String serviceUniqueId) {
        return userRepository.findByServiceUniqueId(serviceUniqueId);
    }

    @Override
    public User findByHandle(String handle) {
        return userRepository.findByHandle(handle);
    }

    @Override
    public User findByUserUuid(String uuid) {
        return userRepository.findByUserUuid(uuid);
    }
}
