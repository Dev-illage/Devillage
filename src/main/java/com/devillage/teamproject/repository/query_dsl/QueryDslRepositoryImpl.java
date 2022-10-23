package com.devillage.teamproject.repository.query_dsl;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.devillage.teamproject.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class QueryDslRepositoryImpl implements QueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Pair<Long, User>> findUserRanking(String p, Pageable pageable) {

        OrderSpecifier<Long> orderSpecifier;
        switch (p) {
            case "point":
                orderSpecifier = user.point.desc();
                break;
            case "post":
                orderSpecifier = user.postCount.desc();
                break;
            case "comment":
                orderSpecifier = user.commentCount.desc();
                break;
            default:
                throw new BusinessLogicException(ExceptionCode.RANKING_PROPERTY_NOT_FOUND);
        }

        List<User> users = jpaQueryFactory
                .selectFrom(user)
                .orderBy(orderSpecifier)
                .limit(pageable.getPageSize())
                .fetch();;
        Long count = jpaQueryFactory
                .select(user.count())
                .from(user)
                .fetchOne();

        if (count == null) {
            throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
        }


        AtomicLong curIdx = new AtomicLong(0L);
        AtomicLong curRanking = new AtomicLong(1L);
        AtomicReference<Long> curPoint = new AtomicReference<>(users.get(0).getPoint());
        List<Pair<Long, User>> pairs;
        switch (p) {
            case "point":
                pairs = users.stream()
                        .map(tempUser -> {
                            curIdx.getAndIncrement();
                            if ((tempUser.getPoint().equals(curPoint.get()))) {
                                return Pair.of(curRanking.get(), tempUser);
                            } else {
                                curPoint.set(tempUser.getPoint());
                                curRanking.set(curIdx.get());
                                return Pair.of(curIdx.get(), tempUser);
                            }
                        })
                        .collect(Collectors.toList());
                break;
            case "post":
                pairs = users.stream()
                        .map(tempUser -> {
                            curIdx.getAndIncrement();
                            if ((tempUser.getPoint().equals(curPoint.get()))) {
                                return Pair.of(curRanking.get(), tempUser);
                            } else {
                                curPoint.set(tempUser.getPostCount());
                                curRanking.set(curIdx.get());
                                return Pair.of(curIdx.get(), tempUser);
                            }
                        })
                        .collect(Collectors.toList());
                break;
            case "comment":
                pairs = users.stream()
                        .map(tempUser -> {
                            curIdx.getAndIncrement();
                            if ((tempUser.getPoint().equals(curPoint.get()))) {
                                return Pair.of(curRanking.get(), tempUser);
                            } else {
                                curPoint.set(tempUser.getCommentCount());
                                curRanking.set(curIdx.get());
                                return Pair.of(curIdx.get(), tempUser);
                            }
                        })
                        .collect(Collectors.toList());
                break;
            default:
                throw new BusinessLogicException(ExceptionCode.RANKING_PROPERTY_NOT_FOUND);
        }


        return new PageImpl<>(pairs.subList(pageable.getPageNumber(), pairs.size()), pageable, count);
    }

}
