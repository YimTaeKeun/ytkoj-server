package com.ytk.ytkoj.domain.problem.dto;

import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseDTOs {
    @Builder
    public record ProblemDetailResponse(
            String title,
            Integer difficulty,
            List<String> tags,
            String content,
            String inputDescription,
            String outputDescription,
            LimitsDTO limits,
            List<ExampleDataDto> exampleData,
            ProblemStatus status
    ){}

    public record ExampleDataDto(
            String inputEx,
            String outputEx
    ){}

    @Builder
    public record ProblemBriefResponse(
            Long problemNumber,
            String title,
            Integer difficulty,
            List<String> tags,
            ProblemStatus status
    ){}

    // 문제 생성 서버에 보낼 레코드
    public record ProblemResponseToGenerator(
            Long problemNumber,
            String title,
            List<String> tags
    ){}
}
