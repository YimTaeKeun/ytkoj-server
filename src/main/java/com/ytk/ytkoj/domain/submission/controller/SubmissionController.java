package com.ytk.ytkoj.domain.submission.controller;

import com.ytk.ytkoj.domain.submission.dto.RequestDTOs;
import com.ytk.ytkoj.domain.submission.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.submission.dto.SubmissionMapper;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.domain.submission.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;
    private final SubmissionMapper submissionMapper;


    @PostMapping("")
    public ResponseEntity<?> submit(@RequestBody RequestDTOs.SubmissionRequest request){
        submissionService.gradingCode(
                request.problemId(),
                request.lang(),
                request.sourceCode(),
                request.revealLevel()
        );
        return ResponseEntity.ok().build();
    }

    @SecurityRequirements
    @GetMapping("")
    public ResponseEntity<Page<?>> getSubmissions(
            @RequestParam(required = false) String handle,
            @RequestParam(required = false) String problemId,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ){
        Page<ResponseDTOs.SubmissionResponse> submission = submissionService.getSubmission(page, handle).map(submissionMapper::toSubmissionResponse);
        return ResponseEntity.ok(submission);
    }

    @SecurityRequirements
    @GetMapping("/detail")
    public ResponseEntity<?> getSubmissionDetail(
            @RequestParam String sId
    ){
        Submission submissionExceptNoReveal = submissionService.getSubmissionExceptNoReveal(sId);
        ResponseDTOs.SubmissionDetailResponse response = submissionMapper.toSubmissionDetailResponse(submissionExceptNoReveal);
        return ResponseEntity.ok(response);
    }

    @SecurityRequirements
    @Operation(
            summary = "채점 결과 회부",
            description = "셀러리에서 채점한 결과를 다시 받아내는 오퍼레이션입니다."
    )
    @PostMapping("/result")
    public void getSubmissionResult(@RequestBody RequestDTOs.GradingResult result){
        submissionService.updateSubmissionStatus(
                result.taskId(),
                result.status(),
                result.message()
        );
    }
}
