package com.ytk.ytkoj.domain.usr.service.internal;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.global.authentication.CustomUserDetails;
import com.ytk.ytkoj.global.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInternalServiceImpl implements UserInternalService{

    @Override
    public User getAuthenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null
                || authentication.getPrincipal() == null
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) throw new UnAuthorizedException("Authentication Required");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
