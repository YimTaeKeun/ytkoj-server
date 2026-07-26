package com.ytk.ytkoj.domain.submission.controller;

import com.ytk.ytkoj.domain.submission.dto.AdminRequestCommands;
import com.ytk.ytkoj.domain.submission.dto.AdminRequests;
import com.ytk.ytkoj.domain.submission.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.submission.dto.SubmissionMapper;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.domain.submission.service.admin.SubmissionAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sub")
public class SubmissionAdminController {

    private final SubmissionAdminService submissionAdminService;
    private final SubmissionMapper submissionMapper;

    @PostMapping("/regrade")
    public ResponseEntity<?> reGrading(
            @RequestBody
            AdminRequests.ReGradingDto dto
    ){
        submissionAdminService.reGrading(
                new AdminRequestCommands.ReGradingCommand(
                        dto.submissionId()
                )
        );
        return ResponseEntity.ok().build();
    }


    @PostMapping("/status")
    public ResponseEntity<ResponseDTOs.SubmissionDetailResponse> modifyStatus(
            @RequestBody
            AdminRequests.ModifyStatusDto dto
    ){
        Submission entity = submissionAdminService.modifyStatus(
                new AdminRequestCommands.ModifyStatusCommand(
                        dto.submissionId(),
                        dto.status()
                )
        );
        ResponseDTOs.SubmissionDetailResponse response = submissionMapper.toSubmissionDetailResponse(entity);
        return ResponseEntity.ok(response);
    }

}
