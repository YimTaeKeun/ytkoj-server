package com.ytk.ytkoj.submission.domain;

import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubmissionTest {

    @Test
    public void submission도메인을_만들어낼수잇다(){
        // given
        String submissionId = "testSub";
        String lang = "python";
        RevealLevel revealLevel = RevealLevel.NO;
        String problemId = "testProblemId";
        String userCode = "print()";
        SubmissionStatus status = SubmissionStatus.PENDING;
        String serverMessage = "serverMessage";
        String userId = "userId";

        // when
        Submission submission = Submission.builder()
                .submissionId(submissionId)
                .lang(lang)
                .revealLevel(revealLevel)
                .problemId(problemId)
                .userCode(userCode)
                .submissionStatus(status)
                .serverMessage(serverMessage)
                .userId(userId)
                .build();
        // then
        assertThat(submission.getSubmissionId()).isEqualTo(submissionId);
        assertThat(submission.getLang()).isEqualTo(lang);
        assertThat(submission.getRevealLevel()).isEqualTo(revealLevel);
        assertThat(submission.getProblemId()).isEqualTo(problemId);
        assertThat(submission.getUserCode()).isEqualTo(userCode);
        assertThat(submission.getSubmissionStatus()).isEqualTo(status);
        assertThat(submission.getServerMessage()).isEqualTo(serverMessage);
        assertThat(submission.getUserId()).isEqualTo(userId);
    }

    @Test
    public void nonnull필드에_null이_들어가면_오류를_뱉어낸다(){
        String submissionId = "testSub";
        String lang = "python";
        RevealLevel revealLevel = RevealLevel.NO;
        String problemId = "testProblemId";
        String userCode = "print()";
        SubmissionStatus status = SubmissionStatus.PENDING;
        String serverMessage = "serverMessage";
        String userId = "userId";
        Submission.SubmissionBuilder submissionBuilder = Submission.builder()
                .submissionId(submissionId)
                .lang(lang)
                .revealLevel(revealLevel)
                .problemId(problemId)
                .userCode(userCode)
                .submissionStatus(status)
                .serverMessage(serverMessage)
                .userId(userId);

        // then
        assertThatThrownBy(
                () -> submissionBuilder.submissionId(null)
                        .build()
        ).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(
                () -> submissionBuilder.userId(null)
                        .build()
        ).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(
                () -> submissionBuilder.problemId(null)
                        .build()
        ).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(
                () -> submissionBuilder.submissionStatus(null)
                        .build()
        ).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(
                () -> submissionBuilder.lang(null)
                        .build()
        ).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(
                () -> submissionBuilder.revealLevel(null)
                        .build()
        ).isInstanceOf(NullPointerException.class);

    }
}
