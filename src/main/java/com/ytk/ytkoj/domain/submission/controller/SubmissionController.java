package com.ytk.ytkoj.domain.submission.controller;

import com.ytk.ytkoj.domain.submission.dto.RequestDTOs;
import com.ytk.ytkoj.domain.submission.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;


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
            @RequestParam(required = false) String problemId
    ){
        return ResponseEntity.noContent().build();
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
