package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.entity.SubmissionStatus;

import java.time.LocalDateTime;

public class ResponseDTOs {
    public record SubmissionResponse(
            String submissionId,
            Long problemId,
            String problemTitle,
            String userHandle,
            SubmissionStatus status,
            String message,
            String createdAt
    ){}
}
