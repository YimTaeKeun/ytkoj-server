package com.ytk.ytkoj.domain.usr.dto;

import com.ytk.ytkoj.domain.usr.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public ResponseDTOs.UserResponse toUserBriefResponse(User user){
        Boolean hasHandle = (user.getHandle() != null);
        return new ResponseDTOs.UserResponse(
                hasHandle,
                user.getHandle(),
                user.getUserUuid(),
                user.getRegisterService()
        );
    }
}
