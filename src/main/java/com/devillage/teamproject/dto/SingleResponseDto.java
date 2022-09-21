package com.devillage.teamproject.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SingleResponseDto<T> {
    private T data;

    public static <T> SingleResponseDto<T> of(T element) {
        return new SingleResponseDto<>(element);
    }
}
