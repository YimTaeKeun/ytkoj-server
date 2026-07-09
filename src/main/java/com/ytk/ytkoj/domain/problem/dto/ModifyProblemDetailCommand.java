package com.ytk.ytkoj.domain.problem.dto;

import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import lombok.Builder;

@Builder
public record ModifyProblemDetailCommand(
        String title, // 제목
        Integer difficulty, // 난이도
        String content, // 문제 내용
        String inputDescription, // 입력 설명
        String outputDescription, // 출력 설명
        String inputEx, // 입력 예시
        String outputEx, // 출력 예시
        ProblemStatus status // 문제 상태
) {
}
