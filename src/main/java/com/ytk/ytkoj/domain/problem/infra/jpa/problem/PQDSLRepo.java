package com.ytk.ytkoj.domain.problem.infra.jpa.problem;

import com.ytk.ytkoj.domain.problem.dto.ProblemSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/***
 * Problem QueryDsl Repository
 */
public interface PQDSLRepo {

    /***
     * 문제를 찾습니다.
     * @param pageable pageable 객체
     * @param condition 조건
     * @return ProblemJpaEntity가 Page 객체에 담겨 반환됩니다.
     */
    Page<ProblemJpaEntity> findProblems(Pageable pageable, ProblemSearchCondition condition);
}
