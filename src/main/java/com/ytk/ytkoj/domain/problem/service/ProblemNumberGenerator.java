package com.ytk.ytkoj.domain.problem.service;

public interface ProblemNumberGenerator {

    /***
     * 사용 가능한 ProblemNumber를 가져옵니다.
     * @return 문제 번호
     */
    Long getNextProblemNumber();
}
