package com.ytk.ytkoj.domain.problem.service;

import com.ytk.ytkoj.domain.problem.entity.ProblemNumberSequence;
import com.ytk.ytkoj.domain.problem.repository.ProblemNumberSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProblemNumberGenerator {
    private final ProblemNumberSequenceRepository repo;


    @Transactional
    public Long getNextProblemNumber(){
        ProblemNumberSequence seq = repo.findById("SEQ").orElse(new ProblemNumberSequence("SEQ", 999L));
        Long nextNumber = seq.getSequence() + 1;
        seq.setSequence(nextNumber);
        ProblemNumberSequence save = repo.save(seq);
        return save.getSequence();
    }
}
