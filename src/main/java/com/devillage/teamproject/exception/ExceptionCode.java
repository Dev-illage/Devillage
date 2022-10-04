package com.devillage.teamproject.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    MALFORMED_JWT_EXCEPTION(401, "Malformed Jwt Exception"),
    UNSUPPORTED_JWT_EXCEPTION(401, "Unsupported Jwt Exception"),
    SIGNATURE_EXCEPTION(401, "Signature Exception"),
    EXPIRED_JWT_EXCEPTION(401, "Expired Jwt Exception"),

    BLOCKED_USER(403, "Blocked User"),

    USER_NOT_FOUND(404, "User not found"),
    POST_NOT_FOUND(404, "Post not found"),
    CATEGORY_NOT_FOUND(404, "Category not found"),
    COMMENT_NOT_FOUND(404, "Comment not found"),
    RE_COMMENT_NOT_FOUND(404, "ReComment not found"),

    EXISTING_USER(409, "Existing User"),
    ALREADY_REPORTED(409, "Already reported"),
    ID_DOES_NOT_MATCH(409, "Id does not match"),

    USER_RESIGNED(422, "Resigned User"),
    USER_AUTHORIZED(403, "Unauthorized user"),
    NOT_VALID_PASSWORD(404,"Not valid password"),
    CAN_NOT_UPDATE_PASSWORD(400,"Can not update password")
    ;

    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
