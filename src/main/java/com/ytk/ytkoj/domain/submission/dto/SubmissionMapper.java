package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.infra.jpa.SubmissionJpaEntity;
import com.ytk.ytkoj.global.util.StringCompressor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubmissionMapper {

    private final StringCompressor stringCompressor;

    public ResponseDTOs.SubmissionResponse toSubmissionResponse(SubmissionJpaEntity submissionJpaEntity){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        byte[] message = submissionJpaEntity.getMessage();
        String decompressedMessage;
        try{
            decompressedMessage = stringCompressor.decompress(message);
        }
        catch (Exception e){
            decompressedMessage = null;
        }

        return new ResponseDTOs.SubmissionResponse(
                submissionJpaEntity.getSubmissionId(),
                submissionJpaEntity.getProblem().getProblemNumber(),
                submissionJpaEntity.getLang(),
                submissionJpaEntity.getProblem().getTitle(),
                submissionJpaEntity.getUser().getHandle(),
                submissionJpaEntity.getStatus(),
                decompressedMessage,
                submissionJpaEntity.getCreatedAt().format(formatter)
        );
    }

    public ResponseDTOs.SubmissionDetailResponse toSubmissionDetailResponse(SubmissionJpaEntity submissionJpaEntity){
        byte[] userCodeRaw = submissionJpaEntity.getUserCode();
        String userCode = stringCompressor.decompress(userCodeRaw);
        ResponseDTOs.SubmissionResponse br = toSubmissionResponse(submissionJpaEntity);
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

    public List<ResponseDTOs.SubmissionResponse> toSubmissionResponseList(List<SubmissionJpaEntity> submissionJpaEntities){
        return submissionJpaEntities.stream().map(this::toSubmissionResponse).toList();
    }
}
