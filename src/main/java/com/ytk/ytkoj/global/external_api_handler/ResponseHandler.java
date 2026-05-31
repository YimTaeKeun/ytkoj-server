package com.ytk.ytkoj.global.external_api_handler;

import com.ytk.ytkoj.global.exception.BadRequestException;
import com.ytk.ytkoj.global.exception.ExternalServerException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ResponseHandler {

    public <T> T response(WebClient.ResponseSpec responseSpec, Class<T> responseType){
        return responseSpec
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> new BadRequestException(resp.statusCode().value() + ": " + body)))
                // 500 번대 오류인 경우, 외부 API 서버의 오류로 판단합니다.
                .onStatus(HttpStatusCode::is5xxServerError, resp ->
                        resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> new ExternalServerException(resp.statusCode().value() + ": " + body)))
                .bodyToMono(responseType)
                .block();
    }
}
