package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.entity.Submission;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SubmissionMapper {

    public ResponseDTOs.SubmissionResponse toSubmissionResponse(Submission submission){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        return new ResponseDTOs.SubmissionResponse(
                submission.getSubmissionId(),
                submission.getProblem().getProblemNumber(),
                submission.getProblem().getTitle(),
                submission.getUser().getHandle(),
                submission.getStatus(),
                submission.getMessage(),
                submission.getCreatedAt().format(formatter)
        );
    }

    public List<ResponseDTOs.SubmissionResponse> toSubmissionResponseList(List<Submission> submissions){
        return submissions.stream().map(this::toSubmissionResponse).toList();
    }
}
