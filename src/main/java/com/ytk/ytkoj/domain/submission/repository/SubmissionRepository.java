package com.ytk.ytkoj.domain.submission.repository;

import com.ytk.ytkoj.domain.submission.entity.RevealLevel;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findBySubmissionId(String submissionId);

    Page<Submission> findAll(Pageable pageable);

    Page<Submission> findAllByUser_Handle(String userHandle, Pageable pageable);

    Optional<Submission> findBySubmissionIdAndRevealLevelNotContains(String submissionId, RevealLevel revealLevel);
}
