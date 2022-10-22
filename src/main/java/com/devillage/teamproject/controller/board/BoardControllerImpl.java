package com.devillage.teamproject.controller.board;

import com.devillage.teamproject.dto.DoubleResponseDto;
import com.devillage.teamproject.dto.RankingDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardControllerImpl implements BoardController {

    private final BoardService boardService;

    @Override
    public DoubleResponseDto<RankingDto> getRanking(String p, int page, int size) {
        Page<Pair<Long, User>> ranking = boardService.getRanking(p, page, size);

        return DoubleResponseDto.of(
                ranking.stream()
                        .map(pair -> RankingDto.of(pair.getSecond(), pair.getFirst()))
                        .collect(Collectors.toList()),
                ranking);
    }

}
