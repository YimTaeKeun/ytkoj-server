package com.ytk.ytkoj.domain.submission.infra;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ytk.ytkoj.domain.submission.domain.RevealLevel;
import com.ytk.ytkoj.domain.submission.domain.Submission;
import com.ytk.ytkoj.domain.submission.domain.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.dto.SubFindCondition;
import com.ytk.ytkoj.domain.submission.infra.jpa.SubmissionJpaEntity;
import com.ytk.ytkoj.global.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static com.ytk.ytkoj.domain.submission.infra.jpa.QSubmissionJpaEntity.submissionJpaEntity;

/**
 * jpa + querydsl을 이용하여 동적으로 쿼리를 생성합니다.
 * */
@Component
@RequiredArgsConstructor
public class QueryDslSubmissionRepositoryImpl implements SubmissionRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Submission findOne(SubFindCondition condition) {
        JPAQuery<SubmissionJpaEntity> query = makeQuery(condition);
        SubmissionJpaEntity entity = null;
        try{
            entity = query.fetchOne();
//            if(entity != null) return entity.to();
        }catch (NonUniqueResultException e){
            throw new InternalServerException("여러 객체가 불러와졌습니다.");
        }
        return null;
    }

    @Override
    public Page<Submission> findWithPage(SubFindCondition condition, Pageable pageable) {
        JPAQuery<SubmissionJpaEntity> query = makeQuery(condition);
        return null;
    }

    private JPAQuery<SubmissionJpaEntity> makeQuery(SubFindCondition condition){
        JPAQuery<SubmissionJpaEntity> subJpa = jpaQueryFactory
                .selectFrom(submissionJpaEntity);
        String subId = condition.submissionId();
        String uH = condition.userHandle();
        RevealLevel neL = condition.notContainLevel();
        RevealLevel eL = condition.containRevealLevel();
        if(subId != null)
            subJpa = subJpa.where(submissionJpaEntity.submissionId.eq(subId));
        if(uH != null)
            subJpa = subJpa.where(submissionJpaEntity.user.handle.eq(uH));
        if(neL != null)
            subJpa = subJpa.where(submissionJpaEntity.revealLevel.ne(neL));
        if(eL != null)
            subJpa = subJpa.where(submissionJpaEntity.revealLevel.eq(eL));

        return subJpa;
    }

    @Override
    public void updateSubmissionStatus(String taskId, SubmissionStatus status, String message) {

    }

    @Override
    public Submission save(Submission submission) {
        return null;
    }
}
