package com.ytk.ytkoj.domain.problem.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GeneratedProblemDTO {
    private String taskId;
    private String title;
    private String content;
    private String inputDescription;
    private String outputDescription;
    private LimitsDTO limits;
    private String inputEx;
    private String outputEx;
    private String gradingDataPath;
}
