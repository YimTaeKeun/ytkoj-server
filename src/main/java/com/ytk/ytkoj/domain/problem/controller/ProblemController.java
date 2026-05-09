package com.ytk.ytkoj.domain.problem.controller;

import com.ytk.ytkoj.domain.problem.dto.GeneratedProblemDTO;
import com.ytk.ytkoj.domain.problem.dto.ProblemMapper;
import com.ytk.ytkoj.domain.problem.dto.RequestDTOs;
import com.ytk.ytkoj.domain.problem.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final ProblemMapper problemMapper; // DTO <-> Entity 변환

    @GetMapping("")
    public ResponseEntity<?> getProblems(
            @RequestParam(required = false, defaultValue = "1") Integer page
    ){
        Page<ResponseDTOs.ProblemBriefResponse> problemBriefResponseStream = problemService.getProblem(page).map(problemMapper::toProblemBriefResponse);
        return ResponseEntity.ok(problemBriefResponseStream);
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<?> getProblemDetail(
            @PathVariable Long problemId
    ){
        Problem problem = problemService.getProblem(problemId);
        ResponseDTOs.ProblemDetailResponse detail = problemMapper.toProblemDetailResponse(problem);
        return ResponseEntity.ok(detail);
    }

    @Operation(
            summary = "생성된 문제 저장",
            description = "셀러리에서 생성된 문제 정보를 받아서 저장합니다."
    )
    @PostMapping("/save-gen")
    public ResponseEntity<?> saveGeneratedProblem(@RequestBody GeneratedProblemDTO request){
        problemService.saveGeneratedProblem(request);
        return ResponseEntity.ok().build();
    }
}
