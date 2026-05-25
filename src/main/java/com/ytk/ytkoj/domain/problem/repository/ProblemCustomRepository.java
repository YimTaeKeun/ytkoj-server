package com.ytk.ytkoj.domain.problem.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.entity.ProblemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ytk.ytkoj.domain.problem.entity.QProblem.problem;
import static com.ytk.ytkoj.domain.problem.entity.QProblemTag.problemTag;
import static com.ytk.ytkoj.domain.problem.entity.QTag.tag;

@Repository
@RequiredArgsConstructor
public class ProblemCustomRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Problem> getProblems(
            Pageable pageable,
            String problemName,
            String[] problemTags,
            String[] asc,
            String[] desc
    ){
        // REMOVED 상태인 문제는 검색이 되지 않도록 막기
        JPAQuery<Problem> baseQuery = queryFactory.selectFrom(problem).where(problem.status.ne(ProblemStatus.REMOVED));
        JPAQuery<Long> countQuery = queryFactory.select(problem.count()).from(problem).where(problem.status.ne(ProblemStatus.REMOVED));

        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(asc, desc);

        // 문제 이름 포함 검색
        if(problemName != null){
            baseQuery = baseQuery
                    .where(problem.title.likeIgnoreCase("%" + problemName + "%"));
            countQuery = countQuery
                    .where(problem.title.likeIgnoreCase("%" + problemName + "%"));
        }


        // 문제 태그 검색
        if(problemTags != null && problemTags.length > 0){
            List<Long> ids = queryFactory
                    .select(problemTag.problem.id)
                    .from(problemTag)
                    .leftJoin(problemTag.tag, tag)
                    .where(tag.tagName.in(problemTags))
                    .fetch();

            baseQuery
                    .distinct()
                    .leftJoin(problem.problemTags, problemTag)
                    // problemTags에 속한 태그 중에 하나라도 있으면 true
                    .where(problem.id.in(ids));
            countQuery
                    .where(problem.id.in(ids));
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

    private OrderSpecifier<?>[] getOrderSpecifiers(String[] asc, String[] desc){
        Map<String, ? extends ComparableExpressionBase<? extends Serializable>> orderList = Map.of(
                "difficulty", problem.difficulty,
                "problemName", problem.title,
                "problemNumber", problem.problemNumber
        );

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        OrderSpecifier<?> problemNumberOrderSpecifier = problem.problemNumber.asc(); // problemNumber는 항상 마지막 정렬로, 기본은 오름차순

        for(String each: asc){
            if ("problemNumber".equals(each)) {
                problemNumberOrderSpecifier = problem.problemNumber.asc();
                continue;
            }

            if(orderList.get(each) != null) orderSpecifiers.add(orderList.get(each).asc());
        }

        for(String each: desc){
            if ("problemNumber".equals(each)) {
                problemNumberOrderSpecifier = problem.problemNumber.desc();
                continue;
            }

            if(orderList.get(each) != null) orderSpecifiers.add(orderList.get(each).desc());
        }

        orderSpecifiers.add(problemNumberOrderSpecifier);

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
