package com.ytk.ytkoj.domain.problem.infra.jpa.pNumber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemNumberSequenceRepository extends JpaRepository<ProblemNumberSequence, String> {
}
