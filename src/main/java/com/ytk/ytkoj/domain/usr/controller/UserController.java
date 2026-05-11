package com.ytk.ytkoj.domain.usr.controller;

import com.ytk.ytkoj.domain.usr.dto.RequestDTOs;
import com.ytk.ytkoj.domain.usr.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.usr.dto.UserMapper;
import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<ResponseDTOs.UserResponse> getUserInfo(){
        User user = userService.authenticateUser();
        ResponseDTOs.UserResponse userResponse = userMapper.toUserBriefResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @SecurityRequirements
    @GetMapping("/check-valid-handle")
    public ResponseEntity<?> checkValidHandle(@RequestParam String handle){
        Optional<User> user = userService.findByHandle(handle);
        String status = user.isPresent() ? "FALSE" : "TRUE";
        return ResponseEntity.ok(status);
    }

    @PostMapping("/handle")
    public ResponseEntity<?> updateHandle(@RequestBody RequestDTOs.UserHandleRequest request){
        User user = userService.updateUserHandle(request.handle());
        ResponseDTOs.UserResponse userBriefResponse = userMapper.toUserBriefResponse(user);
        return ResponseEntity.ok(userBriefResponse);
    }
}
