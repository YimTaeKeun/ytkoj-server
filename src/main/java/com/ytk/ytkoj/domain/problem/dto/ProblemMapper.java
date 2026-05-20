package com.ytk.ytkoj.domain.problem.dto;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.entity.ProblemTag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProblemMapper {

    public ResponseDTOs.ProblemDetailResponse toProblemDetailResponse(Problem problem){
        return new ResponseDTOs.ProblemDetailResponse(
                problem.getTitle(),
                problem.getDifficulty(),
                getProblemTags(problem),
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

    public ResponseDTOs.ProblemBriefResponse toProblemBriefResponse(Problem problem){
        return new ResponseDTOs.ProblemBriefResponse(
                problem.getProblemNumber(),
                problem.getTitle(),
                problem.getDifficulty(),
                getProblemTags(problem),
                problem.getStatus(),
                problem.getCreatedAt(),
                problem.getUpdatedAt()
        );
    }

    private List<String> getProblemTags(Problem problem){
        List<String> ans = new ArrayList<>();
        for(ProblemTag each: problem.getProblemTags()){
            ans.add(each.getTag().getTagName());
        }
        return ans;
    }

    public List<ResponseDTOs.ProblemBriefResponse> toProblemBriefResponseList(List<Problem> problems){
        return problems.stream().map(this::toProblemBriefResponse).toList();
    }
}
