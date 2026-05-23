package com.ytk.ytkoj.domain.problem.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.ytk.ytkoj.domain.problem.dto.GeneratedProblemDTO;
import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;
import com.ytk.ytkoj.domain.problem.entity.ProblemTag;
import com.ytk.ytkoj.domain.problem.entity.Tag;
import com.ytk.ytkoj.domain.problem.repository.ProblemCustomRepository;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.domain.problem.repository.ProblemTagRepository;
import com.ytk.ytkoj.domain.problem.repository.TagRepository;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ytk.ytkoj.domain.problem.entity.QProblem.problem;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemNumberGenerator problemNumberGenerator;
    private final TagRepository tagRepository;
    private final ProblemTagRepository problemTagRepository;
    private final ProblemCustomRepository problemCustomRepository;

    /**
     * 셀러리를 통해서 자동으로 생성된 문제들을 저장합니다.
     * */
    @Transactional
    public void saveGeneratedProblem(GeneratedProblemDTO request){
        Problem problem = getEntity(request);
        Problem savedProblem = problemRepository.save(problem);
        addProblemTag(savedProblem, request.getTags());
    }



    public Problem getProblem(Long problemNumber){
        return problemRepository.findByProblemNumber(problemNumber).orElseThrow(
                () -> new NoResourceException("문제 정보가 존재하지 않습니다.")
        );
    }

    // asc=a,b desc=a,b 이렇게
    public Page<Problem> getProblem(int page, String problemName, String rawAsc, String rawDesc){
        Pageable pageable = Pageable.ofSize(10).withPage(page - 1);
        String[] asc = new String[0], desc = new String[0];
        if(rawAsc != null) asc = rawAsc.replace(" ", "").split(",");
        if(rawDesc != null) desc = rawDesc.replace(" ", "").split(",");

        return problemCustomRepository.getProblems(problemName, pageable, getOrderSpecifiers(asc, desc));
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(String[] asc, String[] desc){
        Map<String, ? extends ComparableExpressionBase<? extends Serializable>> orderList = Map.of(
                "difficulty", problem.difficulty,
                "problemName", problem.title,
                "problemNumber", problem.problemNumber
        );

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        OrderSpecifier<?> problemNumberOrderSpecifier = problem.problemNumber.asc(); // problemNumber는 항상 마지막 정렬로, 기본은 오름차순

        for(String each: asc){
            if ("problemNumber".equals(each)) {
                problemNumberOrderSpecifier = problem.problemNumber.asc();
                continue;
            }

            if(orderList.get(each) != null) orderSpecifiers.add(orderList.get(each).asc());
        }

        for(String each: desc){
            if ("problemNumber".equals(each)) {
                problemNumberOrderSpecifier = problem.problemNumber.desc();
                continue;
            }

            if(orderList.get(each) != null) orderSpecifiers.add(orderList.get(each).desc());
        }

        orderSpecifiers.add(problemNumberOrderSpecifier);

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
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
                ProblemStatus.PENDING, // 검수중
                request.getDifficulty()
        );
    }

    private void addProblemTag(Problem problem, List<String> tags){
        for(String each: tags){
            Tag tag = tagRepository.findByTagName(each)
                    .orElseGet(() -> {
                       Tag newTag = new Tag(each);
                       return tagRepository.save(newTag);
                    });
            ProblemTag problemTag = new ProblemTag(problem, tag);
            ProblemTag save = problemTagRepository.save(problemTag);
            problem.addProblemTag(save);
            problemRepository.save(problem);
        }

    }

}
