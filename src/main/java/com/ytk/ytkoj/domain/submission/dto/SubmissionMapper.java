package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.global.exception.InternalServerException;
import com.ytk.ytkoj.global.util.StringCompressor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubmissionMapper {

    private final StringCompressor stringCompressor;

    public ResponseDTOs.SubmissionResponse toSubmissionResponse(Submission submission){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        byte[] message = submission.getMessage();
        String decompressedMessage;
        try{
            decompressedMessage = stringCompressor.decompress(message);
        }
        catch (Exception e){
            decompressedMessage = null;
        }

        return new ResponseDTOs.SubmissionResponse(
                submission.getSubmissionId(),
                submission.getProblem().getProblemNumber(),
                submission.getLang(),
                submission.getProblem().getTitle(),
                submission.getUser().getHandle(),
                submission.getStatus(),
                decompressedMessage,
                submission.getCreatedAt().format(formatter)
        );
    }

    public ResponseDTOs.SubmissionDetailResponse toSubmissionDetailResponse(Submission submission){
        byte[] userCodeRaw = submission.getUserCode();
        String userCode = stringCompressor.decompress(userCodeRaw);
        ResponseDTOs.SubmissionResponse br = toSubmissionResponse(submission);
        return new ResponseDTOs.SubmissionDetailResponse(
                br.submissionId(),
                br.problemId(),
                br.lang(),
                br.problemTitle(),
                br.userHandle(),
                br.status(),
                br.message(),
                userCode,
                br.createdAt()
        );
    }

    public List<ResponseDTOs.SubmissionResponse> toSubmissionResponseList(List<Submission> submissions){
        return submissions.stream().map(this::toSubmissionResponse).toList();
    }
}
