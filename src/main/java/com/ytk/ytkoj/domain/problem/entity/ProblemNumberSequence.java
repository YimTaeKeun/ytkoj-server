package com.ytk.ytkoj.domain.problem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Problem 번호를 매기기 위한 번호를 알려주는 엔티티 입니다.
 * */
@Entity
@Getter
public class ProblemNumberSequence {
    @Id
    private String id = "SEQ";

    @Setter
    private Long sequence = 999L; // 문제는 1000번 부터 시작
}
