package com.devillage.teamproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
public class MultiResponseDto<T> {
    private List<T> data;
    private PageInfo pageInfo;
    private PostDto.Response.PostDetail response;

    private MultiResponseDto(Page<T> page) {
        this.data = page.getContent();
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public MultiResponseDto(PostDto.Response.PostDetail response) {
        this.response = response;
    }

    public static <T> MultiResponseDto<T> of(Page<T> page) {
        return new MultiResponseDto<>(page);
    }

    public static <T> MultiResponseDto<T> of(PostDto.Response.PostDetail response){
        return new MultiResponseDto<>(response);
    }

}