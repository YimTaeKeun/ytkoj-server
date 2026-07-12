package com.ytk.ytkoj.domain.problem.service.admin;

import com.ytk.ytkoj.domain.problem.dto.ModifyProblemDetailCommand;
import com.ytk.ytkoj.domain.problem.dto.ProblemResponseMapper;
import com.ytk.ytkoj.domain.problem.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaRepository;
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
    private final ProblemJpaRepository problemJpaRepository;
    private final ProblemResponseMapper mapper;

    @Override
    @AdminOnly
    @Transactional
    public ResponseDTOs.ProblemBriefResponse updateProblemStatus(Long problemNumber, ProblemStatus status) {
        ProblemJpaEntity problemJpaEntity = findByProblemNumber(problemNumber);
        problemJpaEntity.setStatus(status);
        ProblemJpaEntity save = problemJpaRepository.save(problemJpaEntity);
        return null;
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
        ProblemJpaEntity problemJpaEntity = findByProblemNumber(problemNumber);

        updateIfNotNull(command.title(), problemJpaEntity::setTitle);
        updateIfNotNull(command.difficulty(), problemJpaEntity::setDifficulty);
        updateIfNotNull(command.content(), problemJpaEntity::setContent);
        updateIfNotNull(command.inputDescription(), problemJpaEntity::setInput_description);
        updateIfNotNull(command.outputDescription(), problemJpaEntity::setOutput_description);
        updateIfNotNull(command.inputEx(), problemJpaEntity::setInput_ex);
        updateIfNotNull(command.outputEx(), problemJpaEntity::setOutput_ex);
        updateIfNotNull(command.status(), problemJpaEntity::setStatus);

        ProblemJpaEntity save = problemJpaRepository.save(problemJpaEntity);

//        return mapper.toProblemDetailResponse(save);
        return null;
    }

    private ProblemJpaEntity findByProblemNumber(Long problemNumber){
        return problemJpaRepository.findById(problemNumber)
                .orElseThrow(() -> new NoResourceException("해당 문제 없음"));
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
