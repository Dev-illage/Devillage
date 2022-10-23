package com.devillage.teamproject.controller.board;

import com.devillage.teamproject.dto.DoubleResponseDto;
import com.devillage.teamproject.dto.RankingDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/board")
public interface BoardController {

    @GetMapping("/ranking")
    @ResponseStatus(HttpStatus.OK)
    DoubleResponseDto<RankingDto> getRanking(@RequestParam String p,
                                             @RequestParam int page,
                                             @RequestParam int size);

}
