package com.ytk.ytkoj.domain.problem.infra.jpa.problemTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemTagRepository extends JpaRepository<ProblemTag, Long> {

    @Query(
            "SELECT pt FROM ProblemTag pt " +
            "JOIN FETCH pt.tag AS t " +
            "WHERE pt.problemJpaEntity.id IN :ids"
    )
    List<ProblemTag> findAllByProblemIds(List<Long> ids);
}
