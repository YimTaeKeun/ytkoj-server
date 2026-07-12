package com.ytk.ytkoj.domain.usr.service.client;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.UserRepository;
import com.ytk.ytkoj.domain.usr.service.internal.UserInternalService;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService{

    private final UserInternalService userInternalService;
    private final UserRepository userRepository;

    @Override
    public User getAuthenticateUser() {
        return userInternalService.getAuthenticateUser();
    }

    @Override
    public Boolean isAvailableHandle(String targetHandle) {
        try{
            userRepository.findByHandle(targetHandle);
        } catch (Exception e){
            return true;
        }
        return false;
    }

    @Override
    public User uploadHandle(String handle) {
        User authenticateUser = getAuthenticateUser();
        authenticateUser.setHandle(handle);
        return userRepository.updateUser(authenticateUser);
    }
}
