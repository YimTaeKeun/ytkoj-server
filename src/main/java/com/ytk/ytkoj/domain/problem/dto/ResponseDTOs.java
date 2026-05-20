package com.ytk.ytkoj.domain.problem.dto;

import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseDTOs {
    public record ProblemDetailResponse(
            String title,
            Integer difficulty,
            List<String> tags,
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
            Integer difficulty,
            List<String> tags,
            ProblemStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){}
}
