package com.ytk.ytkoj.domain.usr.dto;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public ResponseDTOs.UserResponse toUserBriefResponse(User user){
        Boolean hasHandle = (user.getHandle() != null);
        return new ResponseDTOs.UserResponse(
                hasHandle,
                user.getHandle(),
                user.getUserUuid(),
                user.getUserRole(),
                user.getRegisterService()
        );
    }
}
