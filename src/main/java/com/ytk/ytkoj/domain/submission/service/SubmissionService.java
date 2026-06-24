package com.ytk.ytkoj.domain.submission.service;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import com.ytk.ytkoj.domain.submission.infra.jpa.SubmissionJpaEntity;
import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.infra.jpa.SubmissionJpaRepository;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.service.UserServiceDeprecated;
import com.ytk.ytkoj.global.config.aop.UserHandlerCheck;
import com.ytk.ytkoj.global.exception.NoResourceException;
import com.ytk.ytkoj.global.util.StringCompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionJpaRepository submissionJpaRepository;
    private final CeleryTaskManager celeryTaskManager; // 셀러리 작업 전달자
    private final ProblemRepository problemRepository;
    private final UserServiceDeprecated userServiceDeprecated;
    private final StringCompressor stringCompressor;


    public Page<SubmissionJpaEntity> getSubmission(int page, String handle){
        Pageable pageReq = PageRequest.ofSize(10).withPage(page - 1).withSort(Sort.by("createdAt").descending());
        if(handle != null) return submissionJpaRepository.findAllByUser_Handle(handle, pageReq);
        return submissionJpaRepository.findAll(pageReq);
    }

    // 핸들러 있는 유저만 제출 가능
    @UserHandlerCheck
    public void gradingCode(
            Long problemId, // 문제 번호
            String lang, // 작성 언어
            String sourceCode, // 작성된 코드
            RevealLevel revealLevel // 코드 노출 정도
    ){
        // DB로부터 문제 정보를 가져옵니다.
        Problem problem = problemRepository.findByProblemNumber(problemId).orElseThrow(
                () -> new NoResourceException("문제 정보가 없습니다.")
        );
        // 로그인 된 유저를 가져옵니다.
        UserJpaEntity userJpaEntity = userServiceDeprecated.authenticateUser();
        // 채점 정보 데이터를 생성합니다.
        SubmissionJpaEntity submissionJpaEntity = saveSubmissions(userJpaEntity, problem, SubmissionStatus.PENDING, sourceCode, lang, revealLevel);
        String submissionId = submissionJpaEntity.getSubmissionId();

        // 태스크를 전송합니다.
        celeryTaskManager.sendTask(
                submissionId,
                lang,
                sourceCode,
                problem.getGrading_data_path(),
                problem.getTime(),
                problem.getMemory()
        );

    }

    @Transactional
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

    public SubmissionJpaEntity saveSubmissions(UserJpaEntity userJpaEntity, Problem problem, SubmissionStatus status, String userCode, String lang, RevealLevel revealLevel){
        byte[] compressedCode = stringCompressor.compress(userCode);
        SubmissionJpaEntity submissions = new SubmissionJpaEntity(userJpaEntity, problem, status, compressedCode, lang, revealLevel);
        if(revealLevel == null) revealLevel = RevealLevel.NO;
        submissions.setRevealLevel(revealLevel);
        return submissionJpaRepository.save(submissions);
    }

    public SubmissionJpaEntity getSubmissionExceptNoReveal(String submissionId){
        return submissionJpaRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new NoResourceException("채점 기록 찾을 수 없음"));
    }
}
