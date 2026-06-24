package com.ytk.ytkoj.domain.submission.infra.jpa;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * 채점 현황을 나타내는 엔티티입니다.
 * */
@Entity(name = "submission")
@NoArgsConstructor
@Getter
@Setter
public class SubmissionJpaEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Column(nullable = false, unique = true)
    private String submissionId;

    @Lob
    private byte[] message;

    @Lob
    private byte[] userCode; // 압축된 유저 코드

    private String lang; // 유저 사용 언어

    @Enumerated(EnumType.STRING)
    private RevealLevel revealLevel; // 코드 공개 레벨

    public static SubmissionJpaEntity from(
            UserJpaEntity userJpaEntity,
            Problem problem,
            byte[] compressedCode,
            Submission submission
    ){
        return new SubmissionJpaEntity(
                userJpaEntity,
                problem,
                submission.getSubmissionStatus(),
                submission.getSubmissionId(),
                compressedCode,
                submission.getLang(),
                submission.getRevealLevel()
        );
    }

    public Submission to(String decompressedCode, String decompressedMessage){
        return Submission.builder()
                .submissionId(this.submissionId)
                .userId(this.user.getUserUuid())
                .problemId(String.valueOf(this.problem.getId()))
                .submissionStatus(this.status)
                .serverMessage(decompressedMessage)
                .userCode(decompressedCode)
                .lang(this.lang)
                .revealLevel(this.revealLevel)
                .build();
    }

    public SubmissionJpaEntity(UserJpaEntity userJpaEntity, Problem problem, SubmissionStatus status, byte[] userCode, String lang, RevealLevel revealLevel){
        this(userJpaEntity, problem, status, UUID.randomUUID().toString(), userCode, lang, revealLevel);
    }

    public SubmissionJpaEntity(UserJpaEntity user, Problem problem, SubmissionStatus status, String submissionId, byte[] userCode, String lang, RevealLevel revealLevel){
        this.user = user;
        this.status = status;
        this.submissionId = submissionId;
        this.problem = problem;
        this.userCode = userCode;
        this.lang = lang;
        this.revealLevel = revealLevel;
    }
}
