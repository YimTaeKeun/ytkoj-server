package com.ytk.ytkoj.domain.submission.entity;

public enum SubmissionStatus {
    PENDING, // 채점 준비중
    COMPLETE, // 정답
    WRONG, // 틀림
    RUNTIME_ERROR, // 런타임 에러
    TIME_LIMIT_EXCEEDED, // 시간 초과
    MEMORY_LIMIT_EXCEEDED // 메모리 초과
}
