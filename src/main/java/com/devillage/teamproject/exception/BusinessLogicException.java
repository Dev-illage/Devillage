package com.devillage.teamproject.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BusinessLogicException extends RuntimeException {
    public final Map<String, String> validation = new HashMap<>();
    private final ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
