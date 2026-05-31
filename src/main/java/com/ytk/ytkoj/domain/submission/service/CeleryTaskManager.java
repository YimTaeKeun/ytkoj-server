package com.ytk.ytkoj.domain.submission.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytk.ytkoj.global.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 해당 클래스는 셀러리 서버에 실제로 채점 요청을 보내는 역할을 합니다.
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class CeleryTaskManager {
    // 셀러리 프로젝트에서 설정한 task 이름입니다. 셀러리 프로젝트에서 변경사항이 있는게 아니면 건들지 말 것
    private final String taskName = "config.tasks.execute_grading_system";
    // 셀러리에서 설정된 큐 이름입니다. 셀러리 측에서 변경사항이 있는게 아니면 아래 문자열은 건드는 게 아닙니다.
    private final String queueName = "grading_server_queue";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private String taskId;

    public void sendTask(String taskId, String lang, String source, String gradingDataPath, Double timeLimit, Integer memoryLimit){
        this.taskId = taskId;
        Object[] args = convertToTaskArgs(lang, source, gradingDataPath, timeLimit, memoryLimit);

        // 메시지 구성
        Map<String, Object> message = new LinkedHashMap<>(); // 실제로 보낼 메시지

        // body 구성
        List<Object> bodyList = Arrays.asList(
                args,
                Map.of(), // kwargs 자리, 추후 필요하면 넣을 것
                Map.of("callbacks", new Object[]{}, "errbacks", new Object[]{}, "chain", new Object[]{}, "chord", ""));
        String bodyJson;
        try{
            bodyJson = objectMapper.writeValueAsString(bodyList); // 파이썬에서 리스트를 출력할 때와 마찬가지로 리스트 형태를 그대로 출력 변환한 걸 문자열로 반환
        } catch (JsonProcessingException e){
            throw new InternalServerException("Json 직렬화 과정에서 에러 발생");
        }

        String bodyBase64 = Base64.getEncoder().encodeToString(bodyJson.getBytes(StandardCharsets.UTF_8)); // base64 인코딩


        message.put("body", bodyBase64);
        message.put("content-type", "application/json");
        message.put("content-encoding", "utf-8");

        // headers
        message.put("headers", getHeader());

        // properties
        message.put("properties", getProperties());

        // Redis LPUSH (Celery는 BRPOP으로 소비)
        String messageJson;
        try{
            messageJson = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e){
            throw new InternalServerException("Json 직렬화 과정에서 에러 발생");
        }

        log.info("grading task: {} celery로 전송 중...", taskId);
        redisTemplate.opsForList().leftPush(queueName, messageJson);
    }

    private Map<?, ?> getHeader(){
        Map<String, Object> headers = new LinkedHashMap<>();
        headers.put("lang", "py");
        headers.put("task", taskName);
        headers.put("id", taskId);
        headers.put("retries", 0);
        // root_id와 parent_id는 워크 플로우를 트랙하는 데 도움을 줄 수 있음
        headers.put("root_id", taskId);
        headers.put("parent_id", UUID.randomUUID().toString());
        headers.put("group", UUID.randomUUID().toString());
        headers.put("shadow", "ai problem generating System"); // 별명
        return headers;
    }

    private Map<?, ?> getProperties(){
        Map<String, Object> deliveryInfo = new LinkedHashMap<>();
        deliveryInfo.put("exchange", "");
        deliveryInfo.put("routing_key", queueName);

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("correlation_id", taskId);
        properties.put("reply_to", queueName); // 전달할 큐 이름 기본값은 celery
        properties.put("delivery_mode", 2);
        properties.put("delivery_info", deliveryInfo);
        properties.put("body_encoding", "base64"); // 바디 인코딩 정보
        properties.put("delivery_tag", UUID.randomUUID().toString());
        return properties;
    }



    /**
     * 함수 파라미터로 받은 정보를 배열 형태로 감싸 전달합니다.
     * */
    private Object[] convertToTaskArgs(
            String lang,
            String source,
            String gradingDataPath,
            Double timeLimit,
            Integer memoryLimit
    ){
        return new Object[]{lang, source, gradingDataPath, timeLimit, memoryLimit};
    }
}
