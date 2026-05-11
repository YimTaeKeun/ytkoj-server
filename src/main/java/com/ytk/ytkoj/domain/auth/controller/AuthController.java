package com.ytk.ytkoj.domain.auth.controller;

import com.ytk.ytkoj.domain.auth.dto.AuthDTOs;
import com.ytk.ytkoj.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<AuthDTOs.TokenResponse> loginOrRegister(@RequestBody AuthDTOs.SocialLoginRequest request){
        log.info("요청 받음");
        AuthDTOs.TokenResponse tokenResponse = authService.loginRegister(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody AuthDTOs.LogoutRequest request
    ){
        authService.logout(request.accessToken(), request.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirements
    @PostMapping("/refresh")
    public ResponseEntity<AuthDTOs.TokenResponse> refreshToken(
            @RequestBody AuthDTOs.RefreshRequest request
    ){
        AuthDTOs.TokenResponse tokenResponse = authService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(tokenResponse);
    }
}
