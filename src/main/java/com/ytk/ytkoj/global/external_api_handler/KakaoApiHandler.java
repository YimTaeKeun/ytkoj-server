package com.ytk.ytkoj.global.external_api_handler;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoApiHandler {
    private final ResponseHandler responseHandler;
    private final WebClient kakaoAuthWebClient; // auth.kakao.com
    private final WebClient kakaoApiClient; // kapi.kakao.com

    @Value("${KAKAO_REST_API_KEY}")
    private String KAKAO_REST_API_KEY;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String KAKAO_CLIENT_SECRET;

    public JsonNode requestTokenInfo(String authCode, String redirectUri){
        String uri = "/oauth/token";
        // 데이터 순서를 보장합니다.
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", authCode);
        formData.add("redirect_uri", redirectUri);
        formData.add("client_id", KAKAO_REST_API_KEY);
        formData.add("client_secret", KAKAO_CLIENT_SECRET);
        WebClient.ResponseSpec retrieve = kakaoAuthWebClient
                .post()
                .uri(uri)
                .body(BodyInserters.fromFormData(formData))
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve();
        return responseHandler.response(retrieve, JsonNode.class);

    }


    public JsonNode requestUserInfo(String accessToken){
        String uri = "/v2/user/me";
        WebClient.ResponseSpec retrieve = kakaoApiClient
                .post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve();
        return responseHandler.response(retrieve, JsonNode.class);

    }
}
