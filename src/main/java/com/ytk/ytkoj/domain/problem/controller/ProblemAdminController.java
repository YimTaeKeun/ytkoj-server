package com.ytk.ytkoj.domain.problem.controller;

import com.ytk.ytkoj.domain.problem.dto.ModifyProblemDetailCommand;
import com.ytk.ytkoj.domain.problem.dto.ProblemAdminDTOs;
import com.ytk.ytkoj.domain.problem.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.problem.service.admin.ProblemAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/problem")
@RequiredArgsConstructor
public class ProblemAdminController {
    private final ProblemAdminService problemAdminService;


    @PostMapping("/{problemId}/status")
    public ResponseEntity<ResponseDTOs.ProblemBriefResponse> updateProblemStatus(
            @PathVariable Long problemId,
            @RequestBody ProblemAdminDTOs.UpdateProblemStatus dto
    ){
        ResponseDTOs.ProblemBriefResponse response = problemAdminService.updateProblemStatus(problemId, dto.problemStatus());
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteProblem(
            @PathVariable Long problemId
    ){
        problemAdminService.deleteProblem(problemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{problemId}")
    public ResponseEntity<ResponseDTOs.ProblemDetailResponse> modifyProblem(
            @PathVariable Long problemId,
            @RequestBody ProblemAdminDTOs.ModifyProblemDetailDTO dto
    ){
        ResponseDTOs.ProblemDetailResponse response = problemAdminService.modifyProblemDetail(
                problemId,
                ModifyProblemDetailCommand.builder()
                        .title(dto.title())
                        .difficulty(dto.difficulty())
                        .content(dto.content())
                        .inputDescription(dto.inputDescription())
                        .outputDescription(dto.outputDescription())
                        .examples(dto.examples())
                        .status(dto.status())
                        .build()
        );
        return ResponseEntity.ok(response);
    }
}
