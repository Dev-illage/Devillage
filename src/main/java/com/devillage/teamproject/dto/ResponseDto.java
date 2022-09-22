package com.devillage.teamproject.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SingleResponseDto<T> {
        private T data;

        public static <T> SingleResponseDto<T> of(T t) {
            return new SingleResponseDto<>(t);
        }
    }
}
