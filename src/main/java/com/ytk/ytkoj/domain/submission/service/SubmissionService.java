package com.ytk.ytkoj.domain.submission.service;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.infra.ProblemDEMapper;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaRepository;
import com.ytk.ytkoj.domain.problem.service.client.ProblemClientService;
import com.ytk.ytkoj.domain.submission.entity.RevealLevel;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.domain.submission.entity.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.repository.SubmissionRepository;
import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.UserRepository;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import com.ytk.ytkoj.domain.usr.service.internal.UserInternalService;
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
    private final SubmissionRepository submissionRepository;
    private final CeleryTaskManager celeryTaskManager; // 셀러리 작업 전달자
    private final ProblemJpaRepository problemJpaRepository;
    private final UserInternalService userService;
    private final StringCompressor stringCompressor;
    private final UserJpaRepository userJpaRepository;


    public Page<Submission> getSubmission(int page, String handle){
        Pageable pageReq = PageRequest.ofSize(10).withPage(page - 1).withSort(Sort.by("createdAt").descending());
        if(handle != null) return submissionRepository.findAllByUser_Handle(handle, pageReq);
        return submissionRepository.findAll(pageReq);
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
        ProblemJpaEntity problemJpaEntity = problemJpaRepository.findByProblemNumber(problemId).orElseThrow(
                () -> new NoResourceException("문제 정보가 없습니다.")
        );
        // 로그인 된 유저를 가져옵니다.
        User user = userService.getAuthenticateUser();
        // 채점 정보 데이터를 생성합니다.
        UserJpaEntity entity = userJpaRepository.findByUserUuid(user.getUserUuid())
                .orElseThrow(() -> new NoResourceException("찾을 수 없음"));
        Submission submission = saveSubmissions(entity, problemJpaEntity, SubmissionStatus.PENDING, sourceCode, lang, revealLevel);
        String submissionId = submission.getSubmissionId();

        // 태스크를 전송합니다.
        celeryTaskManager.sendTask(
                submissionId,
                lang,
                sourceCode,
                problemJpaEntity.getGrading_data_path(),
                problemJpaEntity.getTime(),
                problemJpaEntity.getMemory()
        );

    }

    @Transactional
    public void updateSubmissionStatus(
            String taskId,
            SubmissionStatus status,
            String message
    ){
        Submission sub = submissionRepository.findBySubmissionId(taskId)
                .orElseThrow(() -> new NoResourceException("채점 기록 찾을 수 없음"));
        sub.setStatus(status);
        if(message != null) sub.setMessage(stringCompressor.compress(message));
        submissionRepository.save(sub);
    }

    public Submission saveSubmissions(UserJpaEntity user, ProblemJpaEntity problemJpaEntity, SubmissionStatus status, String userCode, String lang, RevealLevel revealLevel){
        byte[] compressedCode = stringCompressor.compress(userCode);
        Submission submissions = new Submission(user, problemJpaEntity, status, compressedCode, lang);
        if(revealLevel == null) revealLevel = RevealLevel.NO;
        submissions.setRevealLevel(revealLevel);
        return submissionRepository.save(submissions);
    }

    public Submission getSubmissionExceptNoReveal(String submissionId){
        return submissionRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new NoResourceException("채점 기록 찾을 수 없음"));
    }
}
