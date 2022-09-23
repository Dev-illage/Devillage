package com.devillage.teamproject.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class PageInfo {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static <T> PageInfo of(Page<T> page) {
        return new PageInfo(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
