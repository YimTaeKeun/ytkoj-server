package com.ytk.ytkoj.domain.problem.dto;

import lombok.Builder;

public class RequestDTOs {
    public record GeneratedProblemRequest(
            String taskId,
            String title,
            String content,
            String inputDescription,
            String outputDescription,
            Integer memory,
            Double time,
            String etcLimit,
            String inputEx,
            String outputEx,
            String gradingDataPath
    ){}

    @Builder
    public record FindProblemsCommand(
            int page,
            String problemName,
            String[] problemTags,
            String[] ascList,
            String[] descList
    ){}
}
