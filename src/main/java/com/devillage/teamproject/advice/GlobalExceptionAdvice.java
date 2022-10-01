package com.devillage.teamproject.advice;

import com.devillage.teamproject.dto.ErrorResponseDto;
import com.devillage.teamproject.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleBusinessLogicException(BusinessLogicException e) {
        return new ResponseEntity<>(ErrorResponseDto.of(e), HttpStatus.valueOf(e.getExceptionCode().getStatus()));
    }

}
