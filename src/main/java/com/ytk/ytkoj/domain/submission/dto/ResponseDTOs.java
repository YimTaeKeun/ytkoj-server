package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;

public class ResponseDTOs {
    public record SubmissionResponse(
            String submissionId,
            Long problemId,
            String lang,
            String problemTitle,
            String userHandle,
            SubmissionStatus status,
            String message,
            String createdAt
    ){}

    public record SubmissionDetailResponse(
            String submissionId,
            Long problemId,
            String lang,
            String problemTitle,
            String userHandle,
            SubmissionStatus status,
            String message,
            String userCode,
            String createdAt
    ){}
}
