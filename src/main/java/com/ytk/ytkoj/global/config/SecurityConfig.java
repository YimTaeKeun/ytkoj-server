package com.ytk.ytkoj.global.config;

import com.ytk.ytkoj.global.authentication.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFilter authFilter;

    private final Environment environment;

    @Value("${ALLOWED_ORIGIN_DEV}")
    private String ALLOWED_ORIGIN_DEV;

    @Value("${ALLOWED_ORIGIN_PROD}")
    private String ALLOWED_ORIGIN_PROD;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
//                .cors(AbstractHttpConfigurer::disable)
                .build();
    }

    /**
     * CORS 설정 - 프론트 외 도메인 접속 차단
     * */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        String allowedOrigin;
        if(hasProfile("prod")) allowedOrigin = ALLOWED_ORIGIN_PROD;
        else allowedOrigin = ALLOWED_ORIGIN_DEV;
        String[] originList = getAllowedOrigins(allowedOrigin);
        for(String each: originList) configuration.addAllowedOrigin(each);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(hasProfile("prod"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private boolean hasProfile(String profileName){
        String[] activeProfiles = environment.getActiveProfiles();
        for(String each: activeProfiles) if(each.equals(profileName)) return true;
        return false;
    }

    private String[] getAllowedOrigins(String raw){
        if(!raw.contains(",")) return new String[] {raw};
        return raw.split(",");
    }
}
