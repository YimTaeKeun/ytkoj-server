package com.ytk.ytkoj.domain.usr.controller;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.dto.AdmResDTOs;
import com.ytk.ytkoj.domain.usr.service.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping("")
    public ResponseEntity<List<AdmResDTOs.UserDTO>> getAllUserList(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        List<User> allUser = userAdminService.getAllUser();
        List<AdmResDTOs.UserDTO> list = allUser.stream().map(user -> new AdmResDTOs.UserDTO(
                user.getUserUuid(),
                user.getRegisterService(),
                user.getHandle(),
                user.getUserRole(),
                user.getRegisteredAt().format(formatter)
        )).toList();
        return ResponseEntity.ok(list);
    }
}
