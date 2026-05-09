package com.ytk.ytkoj.domain.problem.dto;

import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;

import java.time.LocalDateTime;

public class ResponseDTOs {
    public record ProblemDetailResponse(
            String title,
            String content,
            String inputDescription,
            String outputDescription,
            LimitsDTO limits,
            String inputEx,
            String outputEx,
            ProblemStatus status
    ){}

    public record ProblemBriefResponse(
            Long problemNumber,
            String title,
            ProblemStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){}
}
