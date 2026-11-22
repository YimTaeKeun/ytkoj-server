package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.entity.SubmissionStatus;

public class AdminRequests {
    /***
     * 제출 상태 정보를 변경하기 위한 커맨드
     * @param submissionId 제출 아이디
     * @param status 변경할 제출 상태
     */
    public record ModifyStatusDto(
            String submissionId,
            SubmissionStatus status
    ){}

    /***
     * 재채점을 위한 커맨드
     * @param submissionId 제출 아이디
     */
    public record ReGradingDto(
            String submissionId
    ){}
}
