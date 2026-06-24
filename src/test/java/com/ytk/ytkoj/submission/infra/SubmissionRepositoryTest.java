package com.ytk.ytkoj.submission.infra;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.dto.SubFindCondition;
import com.ytk.ytkoj.domain.submission.infra.SubmissionRepository;
import com.ytk.ytkoj.domain.submission.infra.SubmissionRepositoryImpl;
import com.ytk.ytkoj.domain.submission.infra.jpa.SubmissionJpaEntity;
import com.ytk.ytkoj.domain.submission.infra.jpa.SubmissionJpaRepository;
import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import com.ytk.ytkoj.global.util.StringCompressor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubmissionRepositoryTest {


    @Mock
    private SubmissionJpaRepository submissionJpaRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private StringCompressor stringCompressor;

    private UserJpaEntity testUser;

    private Problem testProblem;

    private Submission testSubmission;

    private SubmissionRepository submissionRepository;

    @BeforeEach
    public void setUp(){
        submissionRepository = new SubmissionRepositoryImpl(
                submissionJpaRepository,
                userJpaRepository,
                problemRepository,
                stringCompressor
        );

        testProblem = new Problem();

        testUser = UserJpaEntity.fromPojo(User.builder().userUuid("userId").build());



        String submissionId = "subId";
        String userId = "userId";
        String problemId = "2352235";
        SubmissionStatus status = SubmissionStatus.PENDING;
        String userCode = "test";
        String lang = "python";
        RevealLevel revealLevel = RevealLevel.NO;

        testSubmission = Submission.builder()
                .submissionId(submissionId)
                .userId(userId)
                .problemId(problemId)
                .submissionStatus(status)
                .userCode(userCode)
                .lang(lang)
                .revealLevel(revealLevel)
                .build();
    }

    @Test
    public void submission_객체를_저장할_수_있다(){
        // given
        when(stringCompressor.compress(any(String.class)))
                .thenReturn(new byte[] {0, 1});

        when(stringCompressor.decompress(any(byte[].class)))
                .thenReturn("test");

        when(userJpaRepository.findByUserUuid(any(String.class)))
                .thenReturn(Optional.of(testUser));

        when(problemRepository.findByProblemNumber(any(Long.class)))
                .thenReturn(Optional.of(testProblem));

        String submissionId = "subId";
        String userId = "userId";
        String problemId = "2352235";
        SubmissionStatus status = SubmissionStatus.PENDING;
        String userCode = "test";
        String lang = "python";
        RevealLevel revealLevel = RevealLevel.NO;
        Submission submission = Submission.builder()
                .submissionId(submissionId)
                .userId(userId)
                .problemId(problemId)
                .submissionStatus(status)
                .userCode(userCode)
                .lang(lang)
                .revealLevel(revealLevel)
                .build();
        SubmissionJpaEntity testJpaSub = new SubmissionJpaEntity(testUser, testProblem, status, submissionId, userCode.getBytes(), lang, revealLevel);
        when(submissionJpaRepository.save(any(SubmissionJpaEntity.class)))
                .thenReturn(testJpaSub);
        // when
        Submission save = submissionRepository.save(submission);

        // then
        assertThat(save.getSubmissionId()).isEqualTo(submissionId);
        assertThat(save.getUserId()).isEqualTo(userId);
        assertThat(save.getSubmissionStatus()).isEqualTo(status);
        assertThat(save.getUserCode()).isEqualTo(userCode);
        assertThat(save.getLang()).isEqualTo(lang);
        assertThat(save.getRevealLevel()).isEqualTo(revealLevel);
    }

    @Test
    public void submissionId로_객체를_찾을_수_있다(){
        // given
        String submissionId = "subId";
        SubmissionStatus status = SubmissionStatus.PENDING;
        String userCode = "test";
        String lang = "python";
        RevealLevel revealLevel = RevealLevel.NO;

        SubmissionJpaEntity testJpaSub = new SubmissionJpaEntity(
                testUser,
                testProblem,
                status,
                submissionId,
                userCode.getBytes(),
                lang,
                revealLevel
        );

        when(submissionJpaRepository.findBySubmissionIdAndRevealLevelNotContains(
                any(String.class),
                any(RevealLevel.class)
        ))
                .thenReturn(Optional.of(testJpaSub));

        // when
        Submission submission = submissionRepository.findOne(
                SubFindCondition.builder()
                        .submissionId(submissionId)
                        .build()
        );

        // then
        assertThat(submission.getLang()).isEqualTo(testJpaSub.getLang());
        assertThat(submission.getSubmissionId()).isEqualTo(testJpaSub.getSubmissionId());

    }
}
