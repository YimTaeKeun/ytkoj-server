package com.ytk.ytkoj.domain.problem.dto;

import lombok.Builder;

public class RequestDTOs {

    @Builder
    public record FindProblemsCommand(
            int page,
            String problemName,
            String[] problemTags,
            String[] ascList,
            String[] descList
    ){}
}
