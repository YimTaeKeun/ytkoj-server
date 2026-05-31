package com.ytk.ytkoj.domain.problem.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LimitsDTO {
    private Double time;
    private Integer memory;
    private String etcLimit;
}
