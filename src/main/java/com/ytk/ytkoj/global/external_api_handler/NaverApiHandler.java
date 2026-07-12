package com.ytk.ytkoj.global.external_api_handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.ytk.ytkoj.global.external_api_handler.dto.NaverResponseDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class NaverApiHandler {

    @Value("${NAVER_CLIENT_ID}")
    private String NAVER_CLIENT_ID;

    @Value("${NAVER_CLIENT_SECRET}")
    private String NAVER_CLIENT_SECRET;

    private final WebClient naverAuthWebClient;
    private final WebClient naverApiClient;

    private final ResponseHandler responseHandler;

    public NaverResponseDTOs.TokenInfo requestTokenInfo(String authCode){
        // MultiValueMap은 Map과 달리 하나의 키에 여러 키가 존재할 수 있음
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", NAVER_CLIENT_ID);
        body.add("client_secret", NAVER_CLIENT_SECRET);
        body.add("code", authCode);
        // TODO state는 요청 위변조를 막기 위한 것이므로 개발용도로만 ytkoj로 하고 배포시 플로우 바꿀 것
        body.add("state", "ytkoj");


        WebClient.ResponseSpec retrieve = naverAuthWebClient
                .post()
                .uri("/oauth2.0/token")
                .body(BodyInserters.fromFormData(body))
                .retrieve();

        JsonNode data = responseHandler.response(retrieve, JsonNode.class);
        return new NaverResponseDTOs.TokenInfo(
                data.get("access_token").asText()
        );
    }

    public NaverResponseDTOs.UserInfo requestUserInfo(String accessToken){
        WebClient.ResponseSpec response = naverApiClient
                .get()
                .uri("/v1/nid/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve();
        JsonNode data = responseHandler.response(response, JsonNode.class);
        return new NaverResponseDTOs.UserInfo(
                data.get("resultcode").asText(),
                data.get("message").asText(),
                data.get("response").get("id").asText()
        );
    }
}
