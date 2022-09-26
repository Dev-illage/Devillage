package com.devillage.teamproject.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class DoubleResponseDto<T> {
    private final List<T> data;
    private final PageInfo pageInfo;

    private <P> DoubleResponseDto(List<T> data, Page<P> page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public static <T, P> DoubleResponseDto<T> of(List<T> data, Page<P> page) {
        return new DoubleResponseDto<>(data, page);
    }

}
