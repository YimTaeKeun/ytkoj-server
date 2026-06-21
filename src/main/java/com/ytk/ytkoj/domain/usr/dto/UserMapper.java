package com.ytk.ytkoj.domain.usr.dto;

import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public ResponseDTOs.UserResponse toUserBriefResponse(UserJpaEntity userJpaEntity){
        Boolean hasHandle = (userJpaEntity.getHandle() != null);
        return new ResponseDTOs.UserResponse(
                hasHandle,
                userJpaEntity.getHandle(),
                userJpaEntity.getUserUuid(),
                userJpaEntity.getRegisterService()
        );
    }
}
