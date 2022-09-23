package com.devillage.teamproject.controller.exception;

import com.devillage.teamproject.exception.ExceptionCode;

public class JwtAuthenticationException extends RuntimeException {
    ExceptionCode exceptionCode;

    public JwtAuthenticationException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
