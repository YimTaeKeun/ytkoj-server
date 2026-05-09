package com.ytk.ytkoj.domain.problem.service;

import com.ytk.ytkoj.domain.problem.dto.GeneratedProblemDTO;
import com.ytk.ytkoj.domain.problem.dto.RequestDTOs;
import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemNumberGenerator problemNumberGenerator;

    /**
     * 셀러리를 통해서 자동으로 생성된 문제들을 저장합니다.
     * */
    @Transactional
    public void saveGeneratedProblem(GeneratedProblemDTO request){
        Problem problem = getEntity(request);
        problemRepository.save(problem);
    }

    private Problem getEntity(GeneratedProblemDTO request){
        Long problemNumber = problemNumberGenerator.getNextProblemNumber();
        return new Problem(
                problemNumber,
                request.getTaskId(),
                request.getTitle(),
                request.getContent(),
                request.getInputDescription(),
                request.getOutputDescription(),
                request.getLimits().getMemory(),
                request.getLimits().getTime(),
                request.getLimits().getEtcLimit(),
                request.getInputEx(),
                request.getOutputEx(),
                request.getGradingDataPath(),
                ProblemStatus.PENDING // 검수중
        );
    }
}
