package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.entity.RevealLevel;
import com.ytk.ytkoj.domain.submission.entity.SubmissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class RequestDTOs {
    public record SubmissionRequest(
            @Schema(description = "소스 코드 언어 (java, python3...)")
            String lang,
            @Schema(description = "소스코드")
            String sourceCode,
            @Schema(description = "문제 번호")
            Long problemId,
            @Schema(description = "코드 공개 여부")
            RevealLevel revealLevel
    ){}

    public record GradingResult(
            @Schema(description = "taskId")
            String taskId,
            @Schema(description = "채점 상태")
            SubmissionStatus status,
            @Schema(description = "채점 상태 메시지")
            String message
    ){}
}
