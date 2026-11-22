package com.ytk.ytkoj.domain.problem.service.client;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.dto.GeneratedProblemDTO;
import com.ytk.ytkoj.domain.problem.dto.RequestDTOs;
import org.springframework.data.domain.Page;

public interface ProblemClientService {
    /***
     * 문제 생성 서버로부터 받은 문제를 서비스 서버에 등록합니다.
     * @param dto 문제 정보가 들어가 있는 dto
     * @see Problem
     * @return Problem 도메인
     */
    Problem registerNewProblem(GeneratedProblemDTO dto);


    /***
     * 문제들을 가져옵니다.
     * @param command 문제 찾기 위한 조건
     * @return Page로 감싼 Problem 객체들이 반환됩니다.
     */
    Page<Problem> getProblems(RequestDTOs.FindProblemsCommand command);


    /***
     * 문제 번호에 따라 문제 하나의 정보를 가져옵니다.
     * @param problemNumber 문제 번호
     * @see Problem
     * @return Problem 도메인 객체
     */
    Problem getProblem(Long problemNumber);
}
