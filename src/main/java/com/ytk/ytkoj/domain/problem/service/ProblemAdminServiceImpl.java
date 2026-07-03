package com.ytk.ytkoj.domain.problem.service;

import com.ytk.ytkoj.domain.problem.dto.ModifyProblemDetailCommand;
import com.ytk.ytkoj.domain.problem.dto.ProblemMapper;
import com.ytk.ytkoj.domain.problem.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.global.config.aop.AdminOnly;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

/***
 * 관리자용 문제 서비스
 */
@Service
@RequiredArgsConstructor
public class ProblemAdminServiceImpl implements ProblemAdminService{
    private final ProblemRepository problemRepository;
    private final ProblemMapper mapper;

    @Override
    @AdminOnly
    @Transactional
    public ResponseDTOs.ProblemBriefResponse updateProblemStatus(Long problemNumber, ProblemStatus status) {
        Problem problem = findByProblemNumber(problemNumber);
        problem.setStatus(status);
        Problem save = problemRepository.save(problem);
        return mapper.toProblemBriefResponse(save);
    }

    @Override
    @AdminOnly
    @Transactional
    public void deleteProblem(Long problemNumber) {
        updateProblemStatus(problemNumber, ProblemStatus.REMOVED);
    }

    @Override
    @AdminOnly
    public ResponseDTOs.ProblemDetailResponse modifyProblemDetail(Long problemNumber, ModifyProblemDetailCommand command) {
        Problem problem = findByProblemNumber(problemNumber);

        updateIfNotNull(command.title(), problem::setTitle);
        updateIfNotNull(command.difficulty(), problem::setDifficulty);
        updateIfNotNull(command.content(), problem::setContent);
        updateIfNotNull(command.inputDescription(), problem::setInput_description);
        updateIfNotNull(command.outputDescription(), problem::setOutput_description);
        updateIfNotNull(command.inputEx(), problem::setInput_ex);
        updateIfNotNull(command.outputEx(), problem::setOutput_ex);
        updateIfNotNull(command.status(), problem::setStatus);

        Problem save = problemRepository.save(problem);

        return mapper.toProblemDetailResponse(save);
    }

    private Problem findByProblemNumber(Long problemNumber){
        return problemRepository.findById(problemNumber)
                .orElseThrow(() -> new NoResourceException("해당 문제 없음"));
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
