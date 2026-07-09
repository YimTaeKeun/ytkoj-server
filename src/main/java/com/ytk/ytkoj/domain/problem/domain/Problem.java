package com.ytk.ytkoj.domain.problem.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Problem {
    // 문제 번호 - save 요청 시엔 모르는 필드 값
    @Setter
    private Long problemNumber;

    // 셀러리 태스크에 전달될 작업 번호
    private final String taskId;

    // 문제 제목
    private final String title;

    // 문제 내용
    private final String content;

    // 입력 설명
    private final String inputDescription;

    // 출력 설명
    private final String outputDescription;

    // 각종 제한
    private final Limits limits;

    // 채점 데이터 경로
    private final String gradingDataPath;

    // 난이도
    private final Integer difficulty;

    // 태그 정보
    private List<String> tags;

    // 예시 입 출력
    private final List<ExampleData> examples;

    // 문제 상태
    private final ProblemStatus status;

    public void addTag(String tag){
        tags.add(tag);
    }

    public void addTags(List<String> tags){
        this.tags = tags;
    }

    public void addExampleData(ExampleData data){
        examples.add(data);
    }


    @Builder
    public Problem(
            Long problemNumber,
            String taskId,
            String title,
            String content,
            String inputDescription,
            String outputDescription,
            Limits limits,
            String gradingDataPath,
            Integer difficulty,
            List<String> tags,
            List<ExampleData> examples,
            ProblemStatus status
    ) {
        this.problemNumber = problemNumber;
        this.taskId = taskId;
        this.title = title;
        this.content = content;
        this.inputDescription = inputDescription;
        this.outputDescription = outputDescription;
        this.limits = limits;
        this.gradingDataPath = gradingDataPath;
        this.difficulty = difficulty;

        this.tags = (tags != null) ? tags : new ArrayList<>();
        this.examples = (examples != null) ? examples : new ArrayList<>();

        this.status = status;
    }

    /***
     * 각종 제한을 구성합니다.
     * @param memoryLimit 메모리 제한을 의미하며 단위는 mb 입니다.
     * @param timeLimit 시간 제한을 의미하며 단위는 초 (sec) 입니다.
     * @param etcLimit 기타 제한을 의미하며 String 형식입니다.
     */
    @Builder
    public record Limits(Integer memoryLimit, Double timeLimit, String etcLimit) {
    }


    /***
     * 예시 입/출력 데이터를 가져옵니다.
     * @param inputEx 예시 입력
     * @param outputEx 정답 출력
     */
    @Builder
    public record ExampleData(String inputEx, String outputEx){}
}
