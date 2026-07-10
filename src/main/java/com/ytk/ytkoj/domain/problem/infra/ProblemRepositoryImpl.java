package com.ytk.ytkoj.domain.problem.infra;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import com.ytk.ytkoj.domain.problem.dto.ProblemSearchCondition;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.PQDSLRepo;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaRepository;
import com.ytk.ytkoj.domain.problem.infra.jpa.problemTag.ProblemTag;
import com.ytk.ytkoj.domain.problem.infra.jpa.problemTag.ProblemTagRepository;
import com.ytk.ytkoj.domain.problem.infra.jpa.tag.Tag;
import com.ytk.ytkoj.domain.problem.infra.jpa.tag.TagJpaRepository;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository{

    private final ProblemJpaRepository problemJpaRepository;
    private final ProblemDEMapper problemDEMapper;
    private final PQDSLRepo queryDslRepo;

    private final TagJpaRepository tagJpaRepository;
    private final ProblemTagRepository problemTagRepository;

    @Override
    @Transactional
    public Problem save(Problem problem) {
        // Tag 객체 save or get
        List<String> tagString = problem.getTags();
        // 중간 수정이 일어나지 않아야 하므로
        // 수정 불가능한 객체 변환 반환
        List<Tag> tags = tagString.stream()
                .map(this::saveOrGetTag)
                .toList();

        // Problem 객체 저장
        ProblemJpaEntity entity = problemDEMapper.toEntity(problem);
        ProblemJpaEntity save = problemJpaRepository.save(entity);

        // ProblemTag 객체 save
        tags.forEach(tag -> saveProblemTag(save, tag));

        return problemDEMapper.toDomain(save, tagString);
    }

    @Override
    @Transactional
    public Problem update(Problem problem) {
        // TODO 태그 정보 업데이트 반영
        Long problemNumber = problem.getProblemNumber();
        ProblemJpaEntity entity = problemJpaRepository.findByProblemNumber(problemNumber)
                .orElseThrow(() -> new NoResourceException("문제 번호에 해당하는 객체가 없습니다."));


        Problem.Limits limits = problem.getLimits();
        Problem.ExampleData example = problem.getExamples().get(0);

        entity.setTitle(problem.getTitle());
        entity.setContent(problem.getContent());
        entity.setInput_description(problem.getInputDescription());
        entity.setOutput_description(problem.getOutputDescription());
        entity.setMemory(problem.getLimits().memoryLimit());
        entity.setTime(limits.timeLimit());
        entity.setEtcLimit(limits.etcLimit());
        entity.setInput_ex(example.inputEx());
        entity.setOutput_ex(example.outputEx());
        entity.setGrading_data_path(problem.getGradingDataPath());
        entity.setStatus(problem.getStatus());
        entity.setDifficulty(problem.getDifficulty());


        return problemDEMapper.toDomain(entity, problem.getTags());
    }

    @Override
    public Problem findOne(Long problemNumber) {
        ProblemJpaEntity entity = problemJpaRepository.findByProblemNumber(problemNumber)
                .orElseThrow(() -> new NoResourceException("문제 정보를 찾을 수 없습니다."));
        List<String> problemTags =
                problemTagRepository.findAllByProblemIds(List.of(entity.getId()))
                        .stream().map(problemTag -> problemTag.getTag().getTagName())
                        .toList();

        return problemDEMapper.toDomain(entity, problemTags);
    }

    @Override
    public Page<Problem> findByCondition(Pageable pageable, ProblemSearchCondition condition) {

        // 조건에 맞는 문제들을 찾아옴
        Page<ProblemJpaEntity> problems;
        if(condition == null) problems = problemJpaRepository.findAllByOrderByProblemNumberAsc(pageable);
        else problems = queryDslRepo.findProblems(pageable, condition);
        // 아이디 변환
        List<Long> problemIds = problems.stream().map(ProblemJpaEntity::getId).toList();
        // 태그 가져오기
        List<ProblemTag> problemTags = problemTagRepository.findAllByProblemIds(problemIds);

        // Collectors.groupingBy<T, K>는 키가 T, 값이 K인 Map으로 묶어주며,
        Map<Long, List<String>> tagsByProblemId = problemTags.stream()
                .collect(Collectors.groupingBy(
                        pt -> pt.getProblemJpaEntity().getId(),
                        Collectors.mapping(
                                pt -> pt.getTag().getTagName(),
                                Collectors.toList()
                        )
                ));

        return problems.map(
                problem -> problemDEMapper.toDomain(problem, tagsByProblemId.getOrDefault(problem.getId(), List.of()))
        );
    }

    @Override
    @Transactional
    public void delete(Long problemNumber) {
        // soft delete 원칙을 적용합니다.
        ProblemJpaEntity entity = problemJpaRepository.findByProblemNumber(problemNumber)
                .orElseThrow(() -> new NoResourceException("해당 번호의 문제를 찾을 수 없습니다."));
        entity.setStatus(ProblemStatus.REMOVED);
        problemJpaRepository.save(entity);
    }



    private Tag saveOrGetTag(String tagName){
        return tagJpaRepository.findByTagName(tagName)
                .orElseGet(() -> tagJpaRepository.save(new Tag(tagName)));
    }

    private void saveProblemTag(ProblemJpaEntity entity, Tag tag){
        ProblemTag problemTag = new ProblemTag(entity, tag);
        problemTagRepository.save(problemTag);
    }
}
