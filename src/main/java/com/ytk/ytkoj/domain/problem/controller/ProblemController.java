package com.ytk.ytkoj.domain.problem.controller;

import com.ytk.ytkoj.domain.problem.dto.GeneratedProblemDTO;
import com.ytk.ytkoj.domain.problem.dto.RequestDTOs;
import com.ytk.ytkoj.domain.problem.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("")
    public ResponseEntity<?> getProblems(
            @RequestParam(required = false) String problemId,
            @RequestParam(required = false) String page
    ){
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<?> getProblemDetail(
            @PathVariable String problemId
    ){
        return ResponseEntity.noContent().build();
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
