package com.devillage.teamproject.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    USER_NOT_FOUND(404, "User not found"),
    EXISTING_USER(400, "Existing User"),
    POST_NOT_FOUND(404, "Post not found"),
    ALREADY_REPORTED(400, "Already report"),
    MALFORMED_JWT_EXCEPTION(401, "Malformed Jwt Exception"),
    UNSUPPORTED_JWT_EXCEPTION(401, "Unsupported Jwt Exception"),
    SIGNATURE_EXCEPTION(401, "Signature Exception"),
    EXPIRED_JWT_EXCEPTION(401, "Expired Jwt Exception")
    ;
    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
