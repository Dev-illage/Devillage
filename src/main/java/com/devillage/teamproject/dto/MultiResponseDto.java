package com.devillage.teamproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
public class MultiResponseDto<T> {
    private List<T> data;
    private PageInfo pageInfo;

    private <P> MultiResponseDto(List<T> data, Page<P> page) {
        this.data = data;
        this.pageInfo = PageInfo.of(page);
    }

    public static <T, P> MultiResponseDto<T> of(List<T> data, Page<P> page) {
        return new MultiResponseDto<>(data, page);
    }
}
