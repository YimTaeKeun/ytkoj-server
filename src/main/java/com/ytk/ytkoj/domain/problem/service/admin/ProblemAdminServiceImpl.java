package com.ytk.ytkoj.domain.problem.service.admin;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.dto.ModifyProblemDetailCommand;
import com.ytk.ytkoj.domain.problem.dto.ProblemResponseMapper;
import com.ytk.ytkoj.domain.problem.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.problem.infra.ProblemRepository;
import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import com.ytk.ytkoj.domain.problem.service.client.ProblemClientService;
import com.ytk.ytkoj.global.config.aop.AdminOnly;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

/***
 * 관리자용 문제 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "problem")
public class ProblemAdminServiceImpl implements ProblemAdminService{
    private final ProblemRepository problemRepository;
    private final ProblemClientService problemClientService;
    private final ProblemResponseMapper mapper;

    @Override
    @AdminOnly
    @CacheEvict(value = "problemList", allEntries = true) // 캐시에 등록된 problemList 모두 삭제
    public ResponseDTOs.ProblemBriefResponse updateProblemStatus(Long problemNumber, ProblemStatus status) {
        Problem problem = problemRepository.findOne(problemNumber);
        // 문제 상태 변경
        log.info("{}로 변경 시도", status);
        problem.setStatus(status);
        // 상태 변경 이므로 update 실행
        Problem updated = problemRepository.update(problem);
        return mapper.toProblemBriefResponse(updated);
    }

    @Override
    @AdminOnly
    @CacheEvict(value = "problemList", allEntries = true) // 캐시에 등록된 problemList 모두 삭제
    public void deleteProblem(Long problemNumber) {
        updateProblemStatus(problemNumber, ProblemStatus.REMOVED);
    }

    @Override
    @AdminOnly
    @CacheEvict(value = "problemList", allEntries = true) // 캐시에 등록된 problemList 모두 삭제
    public ResponseDTOs.ProblemDetailResponse modifyProblemDetail(Long problemNumber, ModifyProblemDetailCommand command) {
        Problem problem = findByProblemNumber(problemNumber);

        updateIfNotNull(command.title(), problem::setTitle);
        updateIfNotNull(command.difficulty(), problem::setDifficulty);
        updateIfNotNull(command.content(), problem::setContent);
        updateIfNotNull(command.inputDescription(), problem::setInputDescription);
        updateIfNotNull(command.outputDescription(), problem::setOutputDescription);
        updateIfNotNull(command.status(), problem::setStatus);
        if(command.examples() != null){
            List<Problem.ExampleData> list = command.examples().stream().map(
                    each -> Problem.ExampleData.builder()
                            .inputEx(each.inputEx()).outputEx(each.outputEx())
                            .build()
            ).toList();
            problem.setExamples(list);
        }


        Problem update = problemRepository.update(problem);
        return mapper.toProblemDetailResponse(update);
    }

    private Problem findByProblemNumber(Long problemNumber){
        return problemClientService.getProblem(problemNumber);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
