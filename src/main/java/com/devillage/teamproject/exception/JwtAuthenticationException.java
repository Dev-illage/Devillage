package com.devillage.teamproject.exception;

import com.devillage.teamproject.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class JwtAuthenticationException extends RuntimeException {
    ExceptionCode exceptionCode;

    public JwtAuthenticationException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}