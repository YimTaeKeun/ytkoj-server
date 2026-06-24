package com.ytk.ytkoj.domain.submission.infra;

import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.dto.SubFindCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubmissionRepository {
    /**
     * 단 하나의 객체만 반환합니다.
     * */
    Submission findOne(SubFindCondition condition);

    /**
     * 채점 기록을 저장합니다.
     * */
    Submission save(Submission submission);

    /**
     * 특정 조건에 맞는 객체들을 페이지네이션 하여 가져옵니다.
     * */
    Page<Submission> findWithPage(SubFindCondition condition, Pageable pageable);

    /**
     * 제출 상태를 업데이트 합니다. 주로 채점 서버가 해당 기능을 이용합니다.
     * */
    void updateSubmissionStatus(String taskId,
                                SubmissionStatus status,
                                String message);
}
