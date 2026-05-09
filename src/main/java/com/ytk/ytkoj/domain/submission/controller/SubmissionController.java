package com.ytk.ytkoj.domain.submission.controller;

import com.ytk.ytkoj.domain.submission.dto.RequestDTOs;
import com.ytk.ytkoj.domain.submission.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.submission.dto.SubmissionMapper;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.domain.submission.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
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
                request.sourceCode()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<?> getSubmissions(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String problemId,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ){
        Page<ResponseDTOs.SubmissionResponse> submission = submissionService.getSubmission(page).map(submissionMapper::toSubmissionResponse);
        return ResponseEntity.ok(submission);
    }

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
