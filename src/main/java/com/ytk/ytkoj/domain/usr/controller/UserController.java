package com.ytk.ytkoj.domain.usr.controller;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.dto.RequestDTOs;
import com.ytk.ytkoj.domain.usr.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.usr.dto.UserMapper;
import com.ytk.ytkoj.domain.usr.service.client.UserClientService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClientService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<ResponseDTOs.UserResponse> getUserInfo(){
        User userJpaEntity = userService.getAuthenticateUser();
        ResponseDTOs.UserResponse userResponse = userMapper.toUserBriefResponse(userJpaEntity);
        return ResponseEntity.ok(userResponse);
    }

    @SecurityRequirements
    @GetMapping("/check-valid-handle")
    public ResponseEntity<?> checkValidHandle(@RequestParam String handle){
        Boolean avail = userService.isAvailableHandle(handle);
        String status = avail ? "TRUE" : "FALSE";
        return ResponseEntity.ok(status);
    }

    @PostMapping("/handle")
    public ResponseEntity<?> updateHandle(@RequestBody RequestDTOs.UserHandleRequest request){
        User user = userService.uploadHandle(request.handle());
        ResponseDTOs.UserResponse userBriefResponse = userMapper.toUserBriefResponse(user);
        return ResponseEntity.ok(userBriefResponse);
    }
}
