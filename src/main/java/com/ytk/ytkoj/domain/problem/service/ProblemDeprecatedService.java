package com.ytk.ytkoj.domain.problem.service;

import com.ytk.ytkoj.domain.problem.dto.GeneratedProblemDTO;
import com.ytk.ytkoj.domain.problem.infra.jpa.ProblemNumberGenerator;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import com.ytk.ytkoj.domain.problem.infra.jpa.problemTag.ProblemTag;
import com.ytk.ytkoj.domain.problem.infra.jpa.tag.Tag;
import com.ytk.ytkoj.domain.problem.infra.ProblemCustomRepository;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaRepository;
import com.ytk.ytkoj.domain.problem.infra.jpa.problemTag.ProblemTagRepository;
import com.ytk.ytkoj.domain.problem.infra.jpa.tag.TagJpaRepository;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "problem")
public class ProblemDeprecatedService {
    private final ProblemJpaRepository problemJpaRepository;
    private final ProblemNumberGenerator problemNumberGenerator;
    private final TagJpaRepository tagRepository;
    private final ProblemTagRepository problemTagRepository;
    private final ProblemCustomRepository problemCustomRepository;

    /**
     * 셀러리를 통해서 자동으로 생성된 문제들을 저장합니다.
     * */
    @Transactional
    @CacheEvict(value = "problemList", allEntries = true) // 캐시에 등록된 problemList 모두 삭제
    public void saveGeneratedProblem(GeneratedProblemDTO request){
        ProblemJpaEntity problemJpaEntity = getEntity(request);
        ProblemJpaEntity savedProblemJpaEntity = problemJpaRepository.save(problemJpaEntity);
        addProblemTag(savedProblemJpaEntity, request.getTags());
    }



    // TODO 문제 수정 메소드에 @CachePut 메소드 둘 것
    @Cacheable(value = "problem", key = "#problemNumber")
    public ProblemJpaEntity getProblem(Long problemNumber){
        return problemJpaRepository.findByProblemNumber(problemNumber).orElseThrow(
                () -> new NoResourceException("문제 정보가 존재하지 않습니다.")
        );
    }

    // asc=a,b desc=a,b 이렇게
    // page를 제외한 모든 값이 null이 될 수 있으므로 spel에서 elvis operator 표현식을 사용
    @Cacheable(value = "problemList", key = "#page + '|' + (#problemName ?: '') + '|' + (#rawProblemTags ?: '') + '|' + (#rawAsc ?: '') + '|' + (#rawDesc ?: '')")
    public Page<ProblemJpaEntity> getProblem(int page, String problemName, String rawProblemTags, String rawAsc, String rawDesc){
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
        return null;

//        return problemCustomRepository.getProblems(pageable, problemName, problemTags, asc, desc);
    }

    private ProblemJpaEntity getEntity(GeneratedProblemDTO request){
        Long problemNumber = problemNumberGenerator.getNextProblemNumber();
        return new ProblemJpaEntity(
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

    private void addProblemTag(ProblemJpaEntity problemJpaEntity, List<String> tags){
        for(String each: tags){
            Tag tag = tagRepository.findByTagName(each)
                    .orElseGet(() -> {
                       Tag newTag = new Tag(each);
                       return tagRepository.save(newTag);
                    });
            ProblemTag problemTag = new ProblemTag(problemJpaEntity, tag);
            ProblemTag save = problemTagRepository.save(problemTag);
            problemJpaRepository.save(problemJpaEntity);
        }

    }

    public List<ProblemJpaEntity> findByTitle(String title){
        return problemJpaRepository.findAllByTitleContainingIgnoreCase(title);
    }

}
