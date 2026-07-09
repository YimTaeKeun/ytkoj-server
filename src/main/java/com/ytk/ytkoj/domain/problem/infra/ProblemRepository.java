package com.ytk.ytkoj.domain.problem.infra;

import com.ytk.ytkoj.domain.problem.domain.Problem;
import com.ytk.ytkoj.domain.problem.dto.ProblemSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemRepository {

    /***
     * problem 도메인 객체를 서비스에 등록합니다.
     * @param problem Problem 객체
     * @see Problem
     * @return 저장된 Problem 도메인 객체
     */
    Problem save(Problem problem);

    /***
     * 수정된 도메인 객체를 서비스에 업데이트 적용합니다.
     * @param problem Problem 객체
     * @see Problem
     * @return 저장된 Problem 도메인 객체
     */
    Problem update(Problem problem);


    /***
     * problemNumber를 바탕으로 문제 정보를 가져옵니다.
     * @param problemNumber 문제 번호
     * @return Problem 도메인 객체
     */
    Problem findOne(Long problemNumber);

    /***
     * 조건에 따라 문제를 검색합니다.
     * @param pageable pageable 객체
     * @param condition 조건
     * @see Pageable
     * @see ProblemSearchCondition
     * @see Page
     * @return page 객체
     */
    Page<Problem> findByCondition(Pageable pageable, ProblemSearchCondition condition);


    /***
     * 도메인 객체의 문제번호를 이용하여 특정된 Problem 객체를 삭제 조치합니다.
     * @param problemNumber 문제 번호
     */
    void delete(Long problemNumber);
}
