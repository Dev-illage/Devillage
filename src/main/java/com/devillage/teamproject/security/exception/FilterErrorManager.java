package com.devillage.teamproject.security.exception;

import com.devillage.teamproject.exception.ExceptionCode;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class FilterErrorManager {

    public void sendErrorResponse(HttpServletResponse res, ExceptionCode exceptionCode) {
        res.setStatus(exceptionCode.getStatus());
        res.setHeader("error", exceptionCode.getMessage());
    }
}
