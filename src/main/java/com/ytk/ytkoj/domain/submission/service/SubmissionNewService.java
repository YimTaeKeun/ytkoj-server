package com.ytk.ytkoj.domain.submission.service;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.dto.RequestDTOs;
import com.ytk.ytkoj.domain.submission.dto.SubFindCondition;
import com.ytk.ytkoj.domain.submission.infra.SubmissionRepository;
import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.service.UserService;
import com.ytk.ytkoj.global.config.aop.UserHandlerCheck;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionNewService implements SubmissionServiceInterface{
    private final SubmissionRepository submissionRepository;
    private final CeleryTaskManager celeryTaskManager;
    private final ProblemRepository problemRepository; // jpa 레포
    private final UserService userService;


    @Override
    public Submission findOne(String submissionId) {
        return submissionRepository.findOne(SubFindCondition.builder().submissionId(submissionId).build());
    }

    @Override
    public Page<Submission> findAll(int page) {
        return find(page, null);
    }

    @Override
    public Page<Submission> find(int page, SubFindCondition condition) {
        Pageable pageReq = PageRequest.ofSize(10).withPage(page - 1).withSort(Sort.by("createdAt").descending());
        return submissionRepository.findWithPage(condition, pageReq);
    }

    @Override
    public void updateGradingResult(RequestDTOs.GradingResult dto) {
        SubmissionStatus status = dto.status();
        String message = dto.message();
        String taskId = dto.taskId();

        Submission submission = submissionRepository.findOne(SubFindCondition.builder().submissionId(taskId).build());
        submission.setSubmissionStatus(status);
        if(message != null) submission.setServerMessage(message);
        submissionRepository.save(submission);
    }

    @Override
    @UserHandlerCheck
    public void gradingSubmission(RequestDTOs.SubmissionRequest dto) {
        Long problemId = dto.problemId();
        String lang = dto.lang();
        String sourceCode = dto.sourceCode();
        RevealLevel revealLevel = (dto.revealLevel() == null) ? RevealLevel.NO : dto.revealLevel();

        // DB로부터 문제 정보를 가져옵니다.
        Problem problem = problemRepository.findByProblemNumber(problemId).orElseThrow(
                () -> new NoResourceException("문제 정보가 없습니다.")
        );

        // 채점 정보 데이터를 생성합니다.
        Submission submission = saveSubmissions(
                problemId,
                lang,
                sourceCode,
                revealLevel
        );

        String submissionId = submission.getSubmissionId();

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

    private Submission saveSubmissions(Long problemId, String lang, String sourceCode, RevealLevel revealLevel){
        // 로그인 된 유저를 가져옵니다.
        User authenticatedUser = userService.getAuthenticatedUser();
        // 저장할 채점 데이터를 생성합니다.
        Submission submission = Submission.builder()
                .userId(authenticatedUser.getUserUuid())
                .submissionStatus(SubmissionStatus.PENDING)
                .submissionId(UUID.randomUUID().toString())
                .problemId(String.valueOf(problemId))
                .userCode(sourceCode)
                .serverMessage(null)
                .lang(lang)
                .revealLevel(revealLevel)
                .build();
        Submission saved = submissionRepository.save(submission);
        return saved;
    }
}
