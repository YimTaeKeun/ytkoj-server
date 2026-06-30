package com.ytk.ytkoj.domain.submission.service;

import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.dto.RequestDTOs;
import com.ytk.ytkoj.domain.submission.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.submission.dto.SubFindCondition;
import org.springframework.data.domain.Page;

public interface SubmissionServiceInterface {

    // 제출 코드를 채점한다. 제출 결과를 가져온다.

    /***
     * 코드를 채점합니다.
     * @param dto 코드 제출 dto
     */
    void gradingCode(RequestDTOs.SubmissionRequest dto);

    /***
     * 제출 상세 정보를 가져옵니다.
     * @param submissionId - Submission 객체 아이디
     * @return DTO 반환
     */
    ResponseDTOs.SubmissionResponse findSubDetail(String submissionId);

    /***
     * 조건 없이 모든 제출 정보를 찾습니다.
     * @param page 찾을 페이지 번호 (1~)
     * @return
     */
    Page<ResponseDTOs.SubmissionResponse> find(int page);

    /***
     * 조건에 따른 제출 정보를 가져옵니다.
     * @param page 찾을 페이지 번호 (1~)
     * @param condition 조건
     * @return
     */
    Page<ResponseDTOs.SubmissionResponse> find(int page, SubFindCondition condition);

    /***
     * 채점 서버로부터 결과를 가져옵니다.
     * @param dto 결과
     */
    void updateGradingResult(RequestDTOs.GradingResult dto);

}
