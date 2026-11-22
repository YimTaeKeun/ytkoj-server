package com.ytk.ytkoj.domain.submission.service.admin;

import com.ytk.ytkoj.domain.submission.dto.AdminRequestCommands;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.domain.submission.entity.SubmissionStatus;

/***
 * 채점 현황 관리 서비스 (관리자 전용)
 */
public interface SubmissionAdminService {


    /***
     * 제출 상태 정보를 변경합니다.
     * @param command 커맨드
     * @see com.ytk.ytkoj.domain.submission.dto.AdminRequestCommands.ModifyStatusCommand
     */
    Submission modifyStatus(AdminRequestCommands.ModifyStatusCommand command);


    /***
     * 재채점을 진행합니다.
     * @param command 커맨드
     * @see com.ytk.ytkoj.domain.submission.dto.AdminRequestCommands.ReGradingCommand
     */
    void reGrading(AdminRequestCommands.ReGradingCommand command);


}
