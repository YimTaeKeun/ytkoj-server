package com.ytk.ytkoj.domain.problem.service;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import com.ytk.ytkoj.domain.problem.dto.GeneratedProblemDTO;
import com.ytk.ytkoj.domain.problem.dto.LimitsDTO;
import com.ytk.ytkoj.domain.problem.dto.ProblemSearchCondition;
import com.ytk.ytkoj.domain.problem.dto.RequestDTOs;
import com.ytk.ytkoj.domain.problem.infra.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "problem")
public class ProblemClientServiceImpl implements ProblemClientService{

    private final ProblemRepository problemRepository;
    private final ProblemNumberGenerator problemNumberGenerator;

    @Override
    @CacheEvict(value = "problemList", allEntries = true) // 캐시에 등록된 problemList 모두 삭제
    public Problem registerNewProblem(GeneratedProblemDTO dto) {
        Problem problem = dtoToDomain(dto);
        return problemRepository.save(problem);
    }

    @Override
    @Cacheable(value = "problemList", key = "#command.page() + '|' + (#command.problemName() ?: '') + '|' + (#command.problemTags() ?: '') + '|' + (#command.ascList() ?: '') + '|' + (#command.descList() ?: '')")
    public Page<Problem> getProblems(RequestDTOs.FindProblemsCommand command) {
        // TODO 12개로 한정되어 있지만 이 갯수는 외부로부터 주입 받는 방식을 택할 것
        Pageable pageable = Pageable.ofSize(12).withPage(command.page() - 1);
        List<ProblemSearchCondition.SortType> sortTypes = new ArrayList<>();
        if(command.ascList() != null) for(String asc: command.ascList()) sortTypes.add(getSortType(true, asc));

        if(command.descList() != null) for(String desc: command.descList()) sortTypes.add(getSortType(false, desc));


        ProblemSearchCondition condition = ProblemSearchCondition.builder()
                .searchText(command.problemName())
                .searchTagNames((command.problemTags() != null) ? Arrays.stream(command.problemTags()).toList() : null)
                .sortTypes(sortTypes)
                .build();

        return problemRepository.findByCondition(pageable, condition);
    }

    @Override
    // TODO 문제 수정 메소드에 @CachePut 메소드 둘 것
    @Cacheable(value = "problem", key = "#problemNumber")
    public Problem getProblem(Long problemNumber) {
        return problemRepository.findOne(problemNumber);
    }

    private ProblemSearchCondition.SortType getSortType(boolean asc, String rawTag){
        Map<String, ProblemSearchCondition.SortType> typeMaps = Map.of(
                "difficulty_asc", ProblemSearchCondition.SortType.DIFFICULTY_ASC,
                "problemName_asc", ProblemSearchCondition.SortType.PNAME_ASC,
                "problemNumber_asc", ProblemSearchCondition.SortType.PNUM_ASC,
                "difficulty_desc", ProblemSearchCondition.SortType.DIFFICULTY_DESC,
                "problemName_desc", ProblemSearchCondition.SortType.PNAME_DESC,
                "problemNumber_desc", ProblemSearchCondition.SortType.PNUM_DESC
        );
        String postFix = (asc) ? "asc" : "desc";
        return typeMaps.get(rawTag + "_" + postFix);
    }

    private Problem dtoToDomain(GeneratedProblemDTO dto){
        // TODO 단 하나의 예제만 들어가도록 되어 있음

        LimitsDTO limitsDTO = dto.getLimits();
        Problem.ExampleData data = Problem.ExampleData.builder()
                .inputEx(dto.getInputEx())
                .outputEx(dto.getOutputEx())
                .build();


        return Problem.builder()
                .problemNumber(problemNumberGenerator.getNextProblemNumber())
                .taskId(dto.getTaskId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .inputDescription(dto.getInputDescription())
                .outputDescription(dto.getOutputDescription())
                .limits(
                        Problem.Limits.builder()
                                .memoryLimit(limitsDTO.getMemory())
                                .timeLimit(limitsDTO.getTime())
                                .etcLimit(limitsDTO.getEtcLimit())
                                .build()
                )
                .gradingDataPath(dto.getGradingDataPath())
                .difficulty(dto.getDifficulty())
                .tags(dto.getTags())
                .examples(List.of(data))
                .status(ProblemStatus.PENDING)
                .build();
    }
}
