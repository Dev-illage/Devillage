package com.devillage.teamproject.service.board;

import com.devillage.teamproject.repository.query_dsl.QueryDslRepository;
import com.devillage.teamproject.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final QueryDslRepository queryDslRepository;

    @Override
    public Page<Pair<Long, User>> getRanking(String p, int page, int size) {
        return queryDslRepository.findUserRanking(p, PageRequest.of(page - 1, size));
    }
}
