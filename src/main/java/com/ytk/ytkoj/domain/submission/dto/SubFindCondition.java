package com.ytk.ytkoj.domain.submission.dto;

import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import lombok.Builder;

/**
 * 제출 history를 가져오기 위한 조건
 * @param submissionId 제출 아이디
 * @param userHandle 유저 고유 아이디
 * @param notContainLevel 포함하고 싶지 않은 RevealLevel
 * @param containRevealLevel 포함하고 싶은 RevealLevel
 * */
@Builder
public record SubFindCondition(
        String submissionId,
        String userHandle,
        RevealLevel notContainLevel,
        RevealLevel containRevealLevel
){

}
