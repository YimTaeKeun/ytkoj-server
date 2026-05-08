package com.ytk.ytkoj.domain.submission.controller;

import com.ytk.ytkoj.domain.submission.dto.RequestDTOs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    @PostMapping("")
    public ResponseEntity<?> submit(@RequestBody RequestDTOs.SubmissionRequest request){
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<?> getSubmissions(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String problemId
    ){
        return ResponseEntity.noContent().build();
    }
}
