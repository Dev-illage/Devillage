package com.devillage.teamproject.controller.exception;

import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.JwtAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorResponse> jwtException(JwtAuthenticationException exception) {
        log.warn("error : ", exception);

        return ResponseEntity.status(exception.getExceptionCode().getStatus())
                .body(ErrorResponse.of(exception.getExceptionCode()));
    }

    @ResponseBody
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> customException(BusinessLogicException exception) {
        log.warn("error : ", exception);

        return ResponseEntity.status(exception.getExceptionCode().getStatus())
                .body(ErrorResponse.of(exception));
    }
}
