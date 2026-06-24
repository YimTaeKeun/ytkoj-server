package com.ytk.ytkoj.domain.submission.infra.jpa;

import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionJpaRepository extends JpaRepository<SubmissionJpaEntity, Long> {
    Optional<SubmissionJpaEntity> findBySubmissionId(String submissionId);

    Page<SubmissionJpaEntity> findAll(Pageable pageable);

    Page<SubmissionJpaEntity> findAllByUser_Handle(String userHandle, Pageable pageable);

    Optional<SubmissionJpaEntity> findBySubmissionIdAndRevealLevelNotContains(String submissionId, RevealLevel revealLevel);
}
