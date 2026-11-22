package com.ytk.ytkoj.domain.usr.service.admin;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.UserRepository;
import com.ytk.ytkoj.global.config.aop.AdminOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService{

    private final UserRepository userRepository;

    @AdminOnly
    @Override
    public List<User> getAllUser() {
        return userRepository.findAllUser();
    }
}
