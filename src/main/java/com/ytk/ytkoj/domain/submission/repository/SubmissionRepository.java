package com.ytk.ytkoj.domain.submission.repository;

import com.ytk.ytkoj.domain.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findBySubmissionId(String submissionId);
}
