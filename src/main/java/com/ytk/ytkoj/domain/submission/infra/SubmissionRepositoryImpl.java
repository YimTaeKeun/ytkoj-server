package com.ytk.ytkoj.domain.submission.infra;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.dto.SubFindCondition;
import com.ytk.ytkoj.domain.submission.infra.jpa.SubmissionJpaEntity;
import com.ytk.ytkoj.domain.submission.infra.jpa.SubmissionJpaRepository;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import com.ytk.ytkoj.global.exception.InternalServerException;
import com.ytk.ytkoj.global.exception.NoResourceException;
import com.ytk.ytkoj.global.util.StringCompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Jpa로 일일이 구현을 들어갑니다.
 * TODO 추후 querydsl 컴포넌트로 수정합니다.
 * */
@Primary
@Component
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepository{

    private final SubmissionJpaRepository submissionJpaRepository;
    // 유저 jpa 레포 의존
    private final UserJpaRepository userJpaRepository;
    // 문제 jpa 레포 의존
    private final ProblemRepository problemRepository;

    // 문자열 압축 용도
    private final StringCompressor stringCompressor;

    @Override
    public Submission findOne(SubFindCondition condition) {
        if(condition.submissionId() != null) return findBySubmissionId(condition.submissionId());
        return null;
    }

    @Override
    public Page<Submission> findWithPage(SubFindCondition condition, Pageable pageable) {
        if(condition == null) return submissionJpaRepository.findAll(pageable).map(this::convertToDomain);
        if(condition.userHandle() != null) {
            return submissionJpaRepository.findAllByUser_Handle(condition.userHandle(), pageable).map(this::convertToDomain);
        }
        return submissionJpaRepository.findAll(pageable).map(this::convertToDomain);
    }

    private Submission findBySubmissionId(String submissionId){
        SubmissionJpaEntity entity = submissionJpaRepository.findBySubmissionIdAndRevealLevelNotContains(submissionId, RevealLevel.NO)
                .orElseThrow(() -> new NoResourceException("해당 제출 기록을 찾을 수 없습니다."));
        DecompressedResult result = decompress(entity);
        return entity.to(
                result.userCode(),
                result.serverMessage()
        );
    }


    @Transactional
    @Override
    public void updateSubmissionStatus(
            String taskId,
            SubmissionStatus status,
            String message
    ){
        SubmissionJpaEntity sub = submissionJpaRepository.findBySubmissionId(taskId)
                .orElseThrow(() -> new NoResourceException("채점 기록 찾을 수 없음"));
        sub.setStatus(status);
        if(message != null) sub.setMessage(stringCompressor.compress(message));
        submissionJpaRepository.save(sub);
    }

    @Override
    public Submission save(Submission submission) {
        SubmissionJpaEntity entity = convertToEntity(submission);
        SubmissionJpaEntity saved = submissionJpaRepository.save(entity);

        return convertToDomain(saved);
    }

    /**
     * 도메인 객체로 변환
     * */
    private Submission convertToDomain(SubmissionJpaEntity entity){
        DecompressedResult result = decompress(entity);
        return entity.to(
                result.userCode,
                result.serverMessage()
        );
    }

    /**
     * 엔티티 객체로 변환
     * */
    private SubmissionJpaEntity convertToEntity(Submission submission){
        // 유저 uuid 반환
        String userId = submission.getUserId();
        // problemId 반환
        String problemId = submission.getProblemId();
        UserJpaEntity userJpaEntity = userJpaRepository.findByUserUuid(userId)
                .orElseThrow(() -> new InternalServerException("유저를 찾을 수 없습니다."));
        Problem problemJpaEntity = problemRepository.findByProblemNumber(Long.parseLong(problemId))
                .orElseThrow(() -> new InternalServerException("문제를 찾을 수 없습니다."));

        byte[] compress = stringCompressor.compress(submission.getUserCode());

        return SubmissionJpaEntity.from(userJpaEntity, problemJpaEntity, compress, submission);
    }

    public DecompressedResult decompress(SubmissionJpaEntity entity){
        return new DecompressedResult(
                (entity.getUserCode() == null) ? null : stringCompressor.decompress(entity.getUserCode()),
                (entity.getMessage() == null) ? null : stringCompressor.decompress(entity.getMessage())
        );
    }

    public record DecompressedResult(
            String userCode,
            String serverMessage
    ){}
}
