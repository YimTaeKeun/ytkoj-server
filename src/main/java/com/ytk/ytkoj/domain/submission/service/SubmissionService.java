package com.ytk.ytkoj.domain.submission.service;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.domain.submission.entity.RevealLevel;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.domain.submission.entity.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.repository.SubmissionRepository;
import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.service.UserService;
import com.ytk.ytkoj.global.config.aop.UserHandlerCheck;
import com.ytk.ytkoj.global.exception.InternalServerException;
import com.ytk.ytkoj.global.exception.NoResourceException;
import com.ytk.ytkoj.global.util.StringCompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final CeleryTaskManager celeryTaskManager; // 셀러리 작업 전달자
    private final ProblemRepository problemRepository;
    private final UserService userService;
    private final StringCompressor stringCompressor;


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
        Problem problem = problemRepository.findByProblemNumber(problemId).orElseThrow(
                () -> new NoResourceException("문제 정보가 없습니다.")
        );
        // 로그인 된 유저를 가져옵니다.
        User user = userService.authenticateUser();
        // 채점 정보 데이터를 생성합니다.
        Submission submission = saveSubmissions(user, problem, SubmissionStatus.PENDING, sourceCode, lang, revealLevel);
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

    @Transactional
    public void updateSubmissionStatus(
            String taskId,
            SubmissionStatus status,
            String message
    ){
        Submission sub = submissionRepository.findBySubmissionId(taskId)
                .orElseThrow(() -> new NoResourceException("채점 기록 찾을 수 없음"));
        sub.setStatus(status);
        sub.setMessage(stringCompressor.compress(message));
        submissionRepository.save(sub);
    }

    public Submission saveSubmissions(User user, Problem problem, SubmissionStatus status, String userCode, String lang, RevealLevel revealLevel){
        byte[] compressedCode = stringCompressor.compress(userCode);
        Submission submissions = new Submission(user, problem, status, compressedCode, lang);
        if(revealLevel == null) revealLevel = RevealLevel.NO;
        submissions.setRevealLevel(revealLevel);
        return submissionRepository.save(submissions);
    }

    public Submission getSubmissionExceptNoReveal(String submissionId){
        return submissionRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new NoResourceException("채점 기록 찾을 수 없음"));
    }
}
