package com.devillage.teamproject.service.board;

import com.devillage.teamproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;

public interface BoardService {
    Page<Pair<Long, User>> getRanking(String p, int page, int size);
}
