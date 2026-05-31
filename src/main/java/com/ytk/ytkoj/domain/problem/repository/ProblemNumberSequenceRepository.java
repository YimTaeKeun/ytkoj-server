package com.ytk.ytkoj.domain.problem.repository;

import com.ytk.ytkoj.domain.problem.entity.ProblemNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemNumberSequenceRepository extends JpaRepository<ProblemNumberSequence, String> {
}
