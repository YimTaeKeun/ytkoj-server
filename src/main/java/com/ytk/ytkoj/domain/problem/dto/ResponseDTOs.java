package com.ytk.ytkoj.domain.problem.dto;

import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;

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
}
