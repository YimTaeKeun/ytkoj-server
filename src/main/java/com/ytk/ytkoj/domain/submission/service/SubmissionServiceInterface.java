package com.ytk.ytkoj.domain.submission.service;

import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.dto.RequestDTOs;
import com.ytk.ytkoj.domain.submission.dto.SubFindCondition;
import org.springframework.data.domain.Page;

public interface SubmissionServiceInterface {

    /**
     * 제출 번호를 바탕으로 단 하나의 제출 정보만 가져옵니다
     * */
    Submission findOne(String submissionId);

    /**
     * 전체 정보를 가져옵니다.
     * */
    Page<Submission> findAll(int page);

    /**
     * 특정 조건을 가지고 객체를 탐색합니다.
     * */
    Page<Submission> find(int page, SubFindCondition condition);

    /**
     * 채점 결과를 업데이트 합니다.
     * */
    void updateGradingResult(RequestDTOs.GradingResult dto);

    /**
     * 사용자가 제출한 코드를 채점합니다.
     * */
    void gradingSubmission(RequestDTOs.SubmissionRequest dto);

}
