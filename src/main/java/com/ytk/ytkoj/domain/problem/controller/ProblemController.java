package com.ytk.ytkoj.domain.problem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problems")
public class ProblemController {

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
}
