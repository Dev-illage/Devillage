package com.devillage.teamproject.repository.query_dsl;

import com.devillage.teamproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

public interface QueryDslRepository {
    Page<Pair<Long, User>> findUserRanking(String p, Pageable pageable);
}
