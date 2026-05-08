package com.ytk.ytkoj.domain.problem.entity;

import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Problem extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private Long problemNumber; // 외부 노출 용 아이디

    @Column
    private String taskId;

    @Column
    private String title;

    @Lob
    private String content;

    @Lob
    private String input_description;

    @Lob
    private String output_description;

    @Column
    private Integer memory;
    @Column
    private Double time;
    @Column
    private String etcLimit;

    @Lob
    private String input_ex;
    @Lob
    private String output_ex;

    @Column
    private String grading_data_path;

    @Enumerated(EnumType.STRING)
    private ProblemStatus status;

    public Problem(
            Long problemNumber,
            String taskId,
            String title,
            String content,
            String input_description,
            String output_description,
            Integer memory, Double time,
            String etcLimit, String input_ex,
            String output_ex,
            String grading_data_path,
            ProblemStatus status
    ) {
        this.problemNumber = problemNumber;
        this.taskId = taskId;
        this.title = title;
        this.content = content;
        this.input_description = input_description;
        this.output_description = output_description;
        this.memory = memory;
        this.time = time;
        this.etcLimit = etcLimit;
        this.input_ex = input_ex;
        this.output_ex = output_ex;
        this.grading_data_path = grading_data_path;
        this.status = status;
    }

    public Problem(String grading_data_path, String taskId){
        this.grading_data_path = grading_data_path;
        this.taskId = taskId;
    }

    public Problem(String taskId){
        this.taskId = taskId;
    }

}
