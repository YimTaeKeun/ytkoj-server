package com.ytk.ytkoj.domain.problem.infra;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * 해당 클래스는 Problem 도메인과 ProblemJapEntity 간의 변환을 중재합니다.
 */
@Component
public class ProblemDEMapper {

    public Problem toDomain(ProblemJpaEntity entity, List<String> problemTags){
        Problem problem = Problem.builder()
                .problemNumber(entity.getProblemNumber())
                .taskId(entity.getTaskId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .inputDescription(entity.getInput_description())
                .outputDescription(entity.getOutput_description())
                .limits(
                        Problem.Limits.builder()
                                .memoryLimit(entity.getMemory())
                                .timeLimit(entity.getTime())
                                .etcLimit(entity.getEtcLimit())
                                .build()
                )
                .gradingDataPath(entity.getGrading_data_path())
                .difficulty(entity.getDifficulty())
                .status(entity.getStatus())
                .tags(problemTags)
                .build();

        Problem.ExampleData exampleData = Problem.ExampleData.builder()
                .inputEx(entity.getInput_ex())
                .outputEx(entity.getOutput_ex())
                .build();
        problem.addExampleData(exampleData);

        return problem;
    }

    public ProblemJpaEntity toEntity(Problem problem){
        Problem.Limits limits = problem.getLimits();
        // db에는 하나의 데이터밖에 안들어감
        Problem.ExampleData example = problem.getExamples().get(0);

        return ProblemJpaEntity.builder()
                .problemNumber(problem.getProblemNumber())
                .taskId(problem.getTaskId())
                .title(problem.getTitle())
                .content(problem.getContent())
                .input_description(problem.getInputDescription())
                .output_description(problem.getOutputDescription())
                .memory(limits.memoryLimit())
                .time(limits.timeLimit())
                .etcLimit(limits.etcLimit())
                .input_ex(example.inputEx())
                .output_ex(example.outputEx())
                .grading_data_path(problem.getGradingDataPath())
                .status(problem.getStatus())
                .difficulty(problem.getDifficulty())
                .build();
    }
}
