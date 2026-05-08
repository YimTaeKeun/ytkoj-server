package com.ytk.ytkoj.domain.submission.entity;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * 채점 현황을 나타내는 엔티티입니다.
 * */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Submission extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Column(nullable = false, unique = true)
    private String submissionId;

    private String message;

    public Submission(User user, Problem problem, SubmissionStatus status){
        this(user, problem, status, UUID.randomUUID().toString());
    }

    public Submission(User user, Problem problem, SubmissionStatus status, String submissionId){
        this.user = user;
        this.status = status;
        this.submissionId = submissionId;
        this.problem = problem;
    }
}
