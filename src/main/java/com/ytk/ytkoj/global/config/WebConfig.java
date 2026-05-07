package com.ytk.ytkoj.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    /**
     * kauth.kakao.com 으로 요청을 보내는 모든 클래스는 해당 인스턴스를 사용합니다.
     * */
    @Bean
    public WebClient kakaoAuthWebClient(){
        return WebClient
                .builder()
                .baseUrl("https://kauth.kakao.com")
                .build();
    }

    /**
     * kapi.kakao.com 으로 요청을 보내는 모든 클래스는 해당 인스턴스를 사용합니다.
     * */
    @Bean
    public WebClient kakaoApiClient(){
        return WebClient
                .builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
    }
}
