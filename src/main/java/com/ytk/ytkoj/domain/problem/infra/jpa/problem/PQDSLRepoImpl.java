package com.ytk.ytkoj.domain.problem.infra.jpa.problem;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ytk.ytkoj.domain.problem.domain.ProblemStatus;
import com.ytk.ytkoj.domain.problem.dto.ProblemSearchCondition;
import com.ytk.ytkoj.domain.problem.infra.jpa.problemTag.QProblemTag;
import com.ytk.ytkoj.domain.problem.infra.jpa.tag.QTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ytk.ytkoj.domain.problem.infra.jpa.problem.QProblemJpaEntity.problemJpaEntity;
import static com.ytk.ytkoj.domain.problem.infra.jpa.tag.QTag.tag;
import static com.ytk.ytkoj.domain.problem.infra.jpa.problemTag.QProblemTag.problemTag;

/***
 * Problem QueryDsl Repository
 */
@Repository
@RequiredArgsConstructor
public class PQDSLRepoImpl implements PQDSLRepo{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<ProblemJpaEntity> findProblems(Pageable pageable, ProblemSearchCondition condition) {
        List<ProblemSearchCondition.SortType> sortTypes = condition.getSortTypes();

        // count 쿼리가 중복되기 때문에
        BooleanExpression[] conditions = buildConditions(condition);

        List<ProblemJpaEntity> fetch = jpaQueryFactory
                .selectFrom(problemJpaEntity)
                // problemTag join
                .leftJoin(problemTag).on(problemTag.problemJpaEntity.eq(problemJpaEntity))
                // tag join
                .leftJoin(tag).on(problemTag.tag.eq(tag))
                .where(
                        conditions
                )
                .distinct()
                // 페이징
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                // order by 추가
                .orderBy((condition.hasSortTypes()) ? getOrders(sortTypes) : new OrderSpecifier<?>[0])
                .fetch();


        Long total = jpaQueryFactory
                .select(problemJpaEntity.countDistinct())
                .from(problemJpaEntity)
                .leftJoin(problemTag).on(problemTag.problemJpaEntity.eq(problemJpaEntity))
                .leftJoin(tag).on(problemTag.tag.eq(tag))
                .where(conditions)
                .fetchOne();

        return new PageImpl<>(fetch, pageable, total != null ? total : 0);
    }

    private BooleanExpression[] buildConditions(ProblemSearchCondition condition) {
        return new BooleanExpression[]{
                titleContains(condition),
                tagNameEq(condition, problemJpaEntity),
                exceptDeletedProblem()
        };
    }

    private BooleanExpression titleContains(ProblemSearchCondition condition){
        String title = condition.getSearchText();
        return (condition.hasSearchText()) ? problemJpaEntity.title.contains(title): null;
    }

    private BooleanExpression exceptDeletedProblem(){
        return problemJpaEntity.status.ne(ProblemStatus.REMOVED);
    }

    private BooleanExpression tagNameEq(ProblemSearchCondition condition, QProblemJpaEntity problem){
        if(!condition.hasSearchTags()) return null;
        QProblemTag subProblemTag = new QProblemTag("subProblemTag");
        QTag subTag = new QTag("subTag");

        List<Long> matchedProblemIds = jpaQueryFactory
                .select(subProblemTag.problemJpaEntity.id)
                .from(subProblemTag)
                .join(subProblemTag.tag, subTag)
                .where(subTag.tagName.in(condition.getSearchTagNames()))
                .groupBy(subProblemTag.problemJpaEntity.id)
                .having(subTag.tagName.countDistinct().eq((long) condition.getSearchTagNames().size()))
                .fetch();

        return problem.id.in(matchedProblemIds);
    }

    private OrderSpecifier<?> getOrder(ProblemSearchCondition.SortType sortType){
        return switch (sortType) {
            case DIFFICULTY_ASC -> problemJpaEntity.difficulty.asc();
            case DIFFICULTY_DESC -> problemJpaEntity.difficulty.desc();
            case PNAME_ASC -> problemJpaEntity.title.asc();
            case PNAME_DESC -> problemJpaEntity.title.desc();
            case PNUM_ASC -> problemJpaEntity.problemNumber.asc();
            case PNUM_DESC -> problemJpaEntity.problemNumber.desc();
        };
    }

    private OrderSpecifier<?>[] getOrders(List<ProblemSearchCondition.SortType> sortTypes){
        return sortTypes.stream().map(this::getOrder).toArray(OrderSpecifier[]::new);
    }


}
