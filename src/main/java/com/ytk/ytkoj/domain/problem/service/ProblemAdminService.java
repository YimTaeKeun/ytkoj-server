package com.ytk.ytkoj.domain.problem.service;

import com.ytk.ytkoj.domain.problem.dto.ModifyProblemDetailCommand;
import com.ytk.ytkoj.domain.problem.dto.ResponseDTOs;
import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;

/***
 * 관리자용 문제 관리 서비스 입니다.
 */
public interface ProblemAdminService {

    /***
     * 문제 상태를 변경합니다.
     * @see ProblemStatus
     * @see com.ytk.ytkoj.domain.problem.dto.ResponseDTOs.ProblemBriefResponse
     * @param problemNumber 문제 번호
     * @param status 문제 상태 정보
     * @return ProblemBriefResponse 반환
     */
    ResponseDTOs.ProblemBriefResponse updateProblemStatus(Long problemNumber, ProblemStatus status);

    /***
     * 문제를 삭제합니다.
     * @param problemNumber 문제 번호
     */
    void deleteProblem(Long problemNumber);


    /***
     * 문제 정보 전체 혹은 일부를 수정합니다.
     * @see ModifyProblemDetailCommand
     * @see com.ytk.ytkoj.domain.problem.dto.ResponseDTOs.ProblemDetailResponse
     * @param command 수정할 매개변수
     * @param problemNumber 문제 번호
     * @return ProblemDetailResponse
     */
    ResponseDTOs.ProblemDetailResponse modifyProblemDetail(Long problemNumber, ModifyProblemDetailCommand command);
}
