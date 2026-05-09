package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.entity.Submission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubmissionMapper {

    public ResponseDTOs.SubmissionResponse toSubmissionResponse(Submission submission){
        return new ResponseDTOs.SubmissionResponse(
                submission.getSubmissionId(),
                submission.getProblem().getProblemNumber(),
                submission.getProblem().getTitle(),
                submission.getUser().getHandle(),
                submission.getStatus(),
                submission.getMessage(),
                submission.getCreatedAt()
        );
    }

    public List<ResponseDTOs.SubmissionResponse> toSubmissionResponseList(List<Submission> submissions){
        return submissions.stream().map(this::toSubmissionResponse).toList();
    }
}
