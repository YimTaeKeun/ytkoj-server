package com.ytk.ytkoj.domain.problem.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public Page<Problem> getProblem(int page, String problemName, String rawProblemTags, String rawAsc, String rawDesc){
        Pageable pageable = Pageable.ofSize(12).withPage(page - 1);
        String[] asc = new String[0], desc = new String[0];
        String[] problemTags = new String[0];
        // 전처리
        if(rawAsc != null && !rawAsc.isEmpty()) asc = rawAsc.replace(" ", "").split(",");
        if(rawDesc != null && !rawDesc.isEmpty()) desc = rawDesc.replace(" ", "").split(",");
        if(rawProblemTags != null && !rawProblemTags.isEmpty()) {
            problemTags = rawProblemTags.split(",");
            // 앞 뒤 공백 제거 -> 먼저 공백 제거 하고 배열로 나누지 않는 이유는 태그명 자체에 띄어쓰기가 있을 수 있기 때문
            for(int i = 0; i < problemTags.length; i++) problemTags[i] = problemTags[i].strip();
        }

        return problemCustomRepository.getProblems(pageable, problemName, problemTags, asc, desc);
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
