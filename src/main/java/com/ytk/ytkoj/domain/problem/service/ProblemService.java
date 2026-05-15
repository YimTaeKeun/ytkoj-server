package com.ytk.ytkoj.domain.problem.service;

import com.ytk.ytkoj.domain.problem.dto.GeneratedProblemDTO;
import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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



    public Problem getProblem(Long problemNumber){
        return problemRepository.findByProblemNumber(problemNumber).orElseThrow(
                () -> new NoResourceException("문제 정보가 존재하지 않습니다.")
        );
    }

    public Page<Problem> getProblem(int page, String problemName){
        // 사용자 입장에서는 페이지를 1번부터 받도록 합니다.
        Pageable pageable = Pageable.ofSize(10).withPage(page - 1);
        if(problemName != null) return problemRepository.findAllByTitleContainingIgnoreCaseOrderByProblemNumberAsc(pageable, problemName);
        return problemRepository.findAllByOrderByProblemNumberAsc(pageable);
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
