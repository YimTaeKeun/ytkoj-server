package com.ytk.ytkoj.domain.problem.dto;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import org.springframework.stereotype.Component;

@Component
public class ProblemMapper {

    public ResponseDTOs.ProblemDetailResponse toProblemDetailResponse(Problem problem){
        return new ResponseDTOs.ProblemDetailResponse(
                problem.getTitle(),
                problem.getContent(),
                problem.getInput_description(),
                problem.getOutput_description(),
                new LimitsDTO(
                        problem.getTime(),
                        problem.getMemory(),
                        problem.getEtcLimit()
                ),
                problem.getInput_ex(),
                problem.getOutput_ex(),
                problem.getStatus()
        );
    }
}
