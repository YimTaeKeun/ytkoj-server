package com.ytk.ytkoj.domain.problem.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import lombok.Builder;

import java.util.List;

public class ProblemAdminDTOs {
    public record UpdateProblemStatus(
            ProblemStatus problemStatus
    ){}

    @Builder
    public record ModifyProblemDetailDTO(
            String title, // 제목
            Integer difficulty, // 난이도
            String content, // 문제 내용
            String inputDescription, // 입력 설명
            String outputDescription, // 출력 설명
            @JsonPropertyDescription("예제 입출력")
            List<IOExample> examples,
            ProblemStatus status // 문제 상태
    ) {
    }
}
