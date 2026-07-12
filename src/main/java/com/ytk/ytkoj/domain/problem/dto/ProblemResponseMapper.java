package com.ytk.ytkoj.domain.problem.dto;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import com.ytk.ytkoj.domain.problem.infra.jpa.problemTag.ProblemTag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProblemResponseMapper {

    public ResponseDTOs.ProblemDetailResponse toProblemDetailResponse(Problem problem){
        Problem.Limits limits = problem.getLimits();
        List<Problem.ExampleData> examples = problem.getExamples();
        List<ResponseDTOs.ExampleDataDto> exData = examples.stream().map(
                each -> new ResponseDTOs.ExampleDataDto(each.inputEx(), each.outputEx())
        ).toList();

        return ResponseDTOs.ProblemDetailResponse.builder()
                .title(problem.getTitle())
                .difficulty(problem.getDifficulty())
                .tags(problem.getTags())
                .content(problem.getContent())
                .inputDescription(problem.getInputDescription())
                .outputDescription(problem.getOutputDescription())
                .limits(
                        LimitsDTO.builder()
                                .time(limits.timeLimit())
                                .memory(limits.memoryLimit())
                                .etcLimit(limits.etcLimit())
                                .build()
                )
                .exampleData(exData)
                .status(problem.getStatus())
                .build();
    }

    public ResponseDTOs.ProblemBriefResponse toProblemBriefResponse(Problem problem){
        return new ResponseDTOs.ProblemBriefResponse(
                problem.getProblemNumber(),
                problem.getTitle(),
                problem.getDifficulty(),
                problem.getTags(),
                problem.getStatus()
        );
    }

    public List<ResponseDTOs.ProblemBriefResponse> toProblemBriefResponseList(List<Problem> problems){
        return problems.stream().map(this::toProblemBriefResponse).toList();
    }
}
