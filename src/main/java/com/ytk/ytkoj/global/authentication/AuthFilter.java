package com.ytk.ytkoj.global.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytk.ytkoj.global.exception.UnAuthorizedException;
import com.ytk.ytkoj.global.jwt.JwtManager;
import com.ytk.ytkoj.global.token.TokenManager;
import com.ytk.ytkoj.global.token.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenManager tokenManager;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Origin: [{}] - '{}' requested.", request.getHeader("Host"), request.getRequestURI());
        String xff = request.getHeader("X-Forwarded-For");
        String ipAddr = (xff != null) ? xff : request.getRemoteAddr();
        log.info("Client IP INFO: [{}]", ipAddr);
        String authorization = request.getHeader("Authorization");
        if(authorization != null){
            try{
                if(!authorization.startsWith("Bearer ")) throw new UnAuthorizedException("INVALID");
                authorization = authorization.substring(7);
                Claims claims = tokenManager.validateToken(authorization, TokenType.ACCESS);
                // jwt 토큰에서 유저의 userUuid는 subject로 사용합니다.
                uploadUser(claims.getSubject());
            } catch (UnAuthorizedException e){
                // 인증 실패 시
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                // 응답 본문에 담길 JSON 메시지 작성
                Map<String, String> errorResponse = new HashMap<>();
                log.debug("unauthorized");
                errorResponse.put("error", "Unauthorized");
                errorResponse.put("message", e.getMessage());

                // 4. JSON 메시지를 응답 스트림에 쓰기
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);
                return;
            }

        }
        doFilter(request, response, filterChain);
    }

    private void uploadUser(String userUuid){
        // DB로부터 유저를 찾아옵니다.
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(userUuid);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
