package com.ytk.ytkoj.domain.submission.entity;

/**
 * 코드 공개 레벨
 * */
public enum RevealLevel {
    NO, // 절대 안됨
    REVEAL_ALWAYS, // 코드 항상 공개
    REVEAL_WHEN_CORRECT // 정답일 때
}
