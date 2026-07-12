package com.ytk.ytkoj.domain.problem.controller;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.dto.*;
import com.ytk.ytkoj.domain.problem.service.client.ProblemClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemClientService problemService;
    private final ProblemResponseMapper problemMapper; // DTO <-> Entity 변환

    @SecurityRequirements
    @GetMapping("")
    public ResponseEntity<?> getProblems(
            @RequestParam(required = false) String problemName,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String asc,
            @RequestParam(required = false) String desc,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ){
        RequestDTOs.FindProblemsCommand command = RequestDTOs.FindProblemsCommand.builder()
                .page(page)
                .problemName(problemName)
                .problemTags((tags != null) ? tags.split(",") : null)
                .ascList((asc != null) ? asc.split(",") : null)
                .descList((desc != null)? desc.split(",") : null)
                .build();

        Page<Problem> problems = problemService.getProblems(command);
        Page<ResponseDTOs.ProblemBriefResponse> result = problems.map(problemMapper::toProblemBriefResponse);

        return ResponseEntity.ok(result);
    }

    @SecurityRequirements
    @GetMapping("/{problemId}")
    public ResponseEntity<?> getProblemDetail(
            @PathVariable Long problemId
    ){
        Problem problem = problemService.getProblem(problemId);
        ResponseDTOs.ProblemDetailResponse response = problemMapper.toProblemDetailResponse(problem);
        return ResponseEntity.ok(response);
    }

    @SecurityRequirements
    @Operation(
            summary = "생성된 문제 저장",
            description = "셀러리에서 생성된 문제 정보를 받아서 저장합니다."
    )
    @PostMapping("/save-gen")
    public ResponseEntity<?> saveGeneratedProblem(@RequestBody GeneratedProblemDTO request){
        LimitsDTO limits = request.getLimits();
        GeneratedProblemDTO dto = GeneratedProblemDTO.builder()
                .taskId(request.getTaskId())
                .title(request.getTitle())
                .content(request.getContent())
                .inputDescription(request.getInputDescription())
                .outputDescription(request.getOutputDescription())
                .limits(
                        LimitsDTO.builder()
                                .time(limits.getTime())
                                .memory(limits.getMemory())
                                .etcLimit(limits.getEtcLimit())
                                .build()
                )
                .inputEx(request.getInputEx())
                .outputEx(request.getOutputEx())
                .gradingDataPath(request.getGradingDataPath())
                .difficulty(request.getDifficulty())
                .tags(request.getTags())
                .build();


        problemService.registerNewProblem(dto);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirements
    @Operation(
            description = "문제 생성기에서 function calling에 활용할 오퍼레이션 입니다."
    )
    @GetMapping("/all")
    public ResponseEntity<List<ResponseDTOs.ProblemResponseToGenerator>> findByTitle(
            @RequestParam String title
    ){
//        List<ProblemJpaEntity> problemJpaEntities = problemService.findByTitle(title);
//        List<ResponseDTOs.ProblemResponseToGenerator> response = problemJpaEntities.stream().map(
//                problem -> {
//                    List<String> tags = new ArrayList<>();
//                    for (ProblemTag each : problem.getProblemTags()) {
//                        tags.add(each.getTag().getTagName());
//                    }
//                    return new ResponseDTOs.ProblemResponseToGenerator(problem.getProblemNumber(), problem.getTitle(), tags);
//                }
//        ).toList();
        return ResponseEntity.ok(null);
    }
}
