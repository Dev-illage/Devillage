package com.devillage.teamproject.dto;

import com.devillage.teamproject.exception.BusinessLogicException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final int status;
    private final String message;

    public static ErrorResponseDto of(BusinessLogicException e) {
        return new ErrorResponseDto(e.getExceptionCode().getStatus(), e.getExceptionCode().getMessage());
    }

}
