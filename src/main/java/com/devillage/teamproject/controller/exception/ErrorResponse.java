package com.devillage.teamproject.controller.exception;

import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {
    private final int code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(int code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String filedName, String errorMessage) {
        this.validation.put(filedName,errorMessage);
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode.getStatus(),
                exceptionCode.getMessage(), null);
    }

    public static ErrorResponse of(BusinessLogicException exception) {
        return new ErrorResponse(exception.getExceptionCode().getStatus(),
                exception.getMessage(),
                exception.getValidation());
    }

    public static ErrorResponse of(HttpStatus status) {
        return new ErrorResponse(status.value(), status.getReasonPhrase(), null);
    }
}
