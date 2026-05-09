package com.ytk.ytkoj.domain.problem.repository;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemNumber(Long problemNumber);
    Page<Problem> findAllByOrderByProblemNumberAsc(Pageable pageable);
}
