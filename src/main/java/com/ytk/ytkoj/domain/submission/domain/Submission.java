package com.ytk.ytkoj.domain.submission.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public class Submission {

    // 제출 아이디
    private final String submissionId;

    // 유저 아이디
    private final String userId;

    // 문제 아이디
    private final String problemId;

    // 채점 상태
    @Setter
    private SubmissionStatus submissionStatus;

    // 압축된 채점 서버 메시지
    @Setter
    private String serverMessage;

    // 압축된 유저 코드
    private final String userCode;

    // 제출 언어
    private final String lang;

    // 코드 공개 레벨
    @Setter
    private RevealLevel revealLevel;


    @Builder
    public Submission(
            @NonNull
            String submissionId,
            @NonNull
            String userId,
            @NonNull
            String problemId,
            @NonNull
            SubmissionStatus submissionStatus,
            String serverMessage,
            String userCode,
            @NonNull
            String lang,
            @NonNull
            RevealLevel revealLevel
    ){
        this.submissionId = submissionId;
        this.userId = userId;
        this.problemId = problemId;
        this.submissionStatus = submissionStatus;
        this.serverMessage = serverMessage;
        this.userCode = userCode;
        this.lang = lang;
        this.revealLevel = revealLevel;
    }

}
