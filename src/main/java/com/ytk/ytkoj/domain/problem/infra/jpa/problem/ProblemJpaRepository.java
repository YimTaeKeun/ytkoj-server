package com.ytk.ytkoj.domain.problem.infra.jpa.problem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProblemJpaRepository extends JpaRepository<ProblemJpaEntity, Long> {
    Optional<ProblemJpaEntity> findByProblemNumber(Long problemNumber);
    Page<ProblemJpaEntity> findAllByOrderByProblemNumberAsc(Pageable pageable);

    Page<ProblemJpaEntity> findAllByTitleContainingIgnoreCaseOrderByProblemNumberAsc(Pageable page, String title);

    List<ProblemJpaEntity> findAllByTitleContainingIgnoreCase(String title);

    List<ProblemJpaEntity> findAllByIdIn(Collection<Long> ids);

}
