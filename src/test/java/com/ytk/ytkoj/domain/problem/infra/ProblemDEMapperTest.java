package com.ytk.ytkoj.domain.problem.infra;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProblemDEMapperTest {

    private ProblemDEMapper mapper;

    @BeforeEach
    public void setUp(){
        this.mapper = new ProblemDEMapper();
    }

    @Test
    public void JPA엔티티_객체를_도메인으로_변환할_수_있다(){
        // given
        Long problemNumber = 1000L; String taskId = "hello";
        String title = "testTitle"; String content = "testContent";
        String inputDescription = "input_desc"; String outputDescription = "output_desc";
        Integer memory = 256; Double time = 12.3;
        String etcLimit = "etc"; String inputEx = "Input";
        String outputEx = "output"; String gradingDataPath = "grading";
        ProblemStatus status = ProblemStatus.ACCEPTED; Integer difficulty = 20;

        ProblemJpaEntity entity = ProblemJpaEntity.builder()
                .problemNumber(problemNumber).taskId(taskId)
                .title(title).content(content)
                .input_description(inputDescription).output_description(outputDescription)
                .memory(memory).time(time).etcLimit(etcLimit)
                .input_ex(inputEx).output_ex(outputEx)
                .grading_data_path(gradingDataPath)
                .status(status)
                .difficulty(difficulty).build();

        List<String> tagList = List.of("stack");

        // when
        Problem domain = mapper.toDomain(entity, tagList);

        // then
        assertThat(domain.getProblemNumber()).isEqualTo(problemNumber);
        assertThat(domain.getTitle()).isEqualTo(title);
        assertThat(domain.getContent()).isEqualTo(content);
        assertThat(domain.getInputDescription()).isEqualTo(inputDescription);
        assertThat(domain.getOutputDescription()).isEqualTo(outputDescription);
        assertThat(domain.getLimits().timeLimit()).isEqualTo(time);
        assertThat(domain.getLimits().memoryLimit()).isEqualTo(memory);
        assertThat(domain.getLimits().etcLimit()).isEqualTo(etcLimit);
        assertThat(domain.getExamples().get(0).inputEx()).isEqualTo(inputEx);
        assertThat(domain.getExamples().get(0).outputEx()).isEqualTo(outputEx);
        assertThat(domain.getGradingDataPath()).isEqualTo(gradingDataPath);
        assertThat(domain.getStatus()).isEqualTo(status);
        assertThat(domain.getDifficulty()).isEqualTo(difficulty);

    }

    @Test
    public void 도메인_객체를_엔티티로_변환할_수_있다(){
        // given
        Long problemNumber = 1000L; String taskId = "hello";
        String title = "testTitle"; String content = "testContent";
        String inputDescription = "input_desc"; String outputDescription = "output_desc";
        Integer memory = 256; Double time = 12.3;
        String etcLimit = "etc"; String inputEx = "Input";
        String outputEx = "output"; String gradingDataPath = "grading";
        ProblemStatus status = ProblemStatus.ACCEPTED; Integer difficulty = 20;

        Problem problem = Problem.builder()
                .problemNumber(problemNumber).taskId(taskId)
                .title(title).content(content)
                .inputDescription(inputDescription).outputDescription(outputDescription)
                .limits(
                        Problem.Limits.builder()
                                .memoryLimit(memory).timeLimit(time).etcLimit(etcLimit)
                                .build()
                ).gradingDataPath(gradingDataPath)
                .difficulty(difficulty).tags(List.of("stack"))
                .examples(List.of(
                        Problem.ExampleData.builder().inputEx(inputEx).outputEx(outputEx).build()
                )).status(status)
                .build();

        // when
        ProblemJpaEntity entity = mapper.toEntity(problem);


        // then
        assertThat(entity.getProblemNumber()).isEqualTo(problemNumber);
        assertThat(entity.getTitle()).isEqualTo(title);
        assertThat(entity.getContent()).isEqualTo(content);
        assertThat(entity.getInput_description()).isEqualTo(inputDescription);
        assertThat(entity.getOutput_description()).isEqualTo(outputDescription);
        assertThat(entity.getTime()).isEqualTo(time);
        assertThat(entity.getMemory()).isEqualTo(memory);
        assertThat(entity.getEtcLimit()).isEqualTo(etcLimit);
        assertThat(entity.getInput_ex()).isEqualTo(inputEx);
        assertThat(entity.getOutput_ex()).isEqualTo(outputEx);
        assertThat(entity.getGrading_data_path()).isEqualTo(gradingDataPath);
        assertThat(entity.getStatus()).isEqualTo(status);
        assertThat(entity.getDifficulty()).isEqualTo(difficulty);

    }
}
