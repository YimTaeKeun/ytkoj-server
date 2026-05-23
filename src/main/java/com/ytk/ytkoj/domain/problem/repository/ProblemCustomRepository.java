package com.ytk.ytkoj.domain.problem.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ytk.ytkoj.domain.problem.entity.Problem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import static com.ytk.ytkoj.domain.problem.entity.QProblem.problem;

@Repository
@RequiredArgsConstructor
public class ProblemCustomRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Problem> getProblems(
        String problemName,
        Pageable pageable,
        OrderSpecifier<?> ... orderSpecifiers
    ){
        JPAQuery<Problem> baseQuery = queryFactory.selectFrom(problem);
        JPAQuery<Long> countQuery = queryFactory.select(problem.count()).from(problem);
        if(problemName != null){
            baseQuery = baseQuery
                    .where(problem.title.likeIgnoreCase("%" + problemName + "%"));


            countQuery = countQuery
                    .where(problem.title.likeIgnoreCase("%" + problemName + "%"));
        }
        baseQuery = baseQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 정렬 적용
        for(OrderSpecifier<?> orderSpecifier: orderSpecifiers) {
            baseQuery.orderBy(orderSpecifier);
        }

        JPAQuery<Long> finalCountQuery = countQuery;
        return PageableExecutionUtils.getPage(baseQuery.fetch(), pageable, finalCountQuery::fetchOne);
    }
}
