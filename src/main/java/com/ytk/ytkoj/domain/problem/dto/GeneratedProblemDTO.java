package com.ytk.ytkoj.domain.problem.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
    private Integer difficulty;
    private List<String> tags;
}
