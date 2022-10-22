package com.devillage.teamproject.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    MALFORMED_JWT_EXCEPTION(401, "Malformed Jwt Exception"),
    UNSUPPORTED_JWT_EXCEPTION(401, "Unsupported Jwt Exception"),
    SIGNATURE_EXCEPTION(401, "Signature Exception"),
    EXPIRED_JWT_EXCEPTION(401, "Expired Jwt Exception"),
    UNAUTHORIZED_FOR_CHATROOM_EXCEPTION(401, "Unauthorized for this chatRoom"),

    BLOCKED_USER(403, "Blocked User"),

    USER_NOT_FOUND(404, "User not found"),
    POST_NOT_FOUND(404, "Post not found"),
    CATEGORY_NOT_FOUND(404, "Category not found"),
    COMMENT_NOT_FOUND(404, "Comment not found"),
    RE_COMMENT_NOT_FOUND(404, "ReComment not found"),
    TAG_NOT_FOUND(404, "Tag not found"),
    REPORT_TYPE_NOT_FOUND(404, "ReportType not found"),
    CHAT_ROOM_NOT_FOUND(404, "ChatRoom not found"),
    CHAT_IN_NOT_FOUND(404, "ChatIn not found"),
    RANKING_PROPERTY_NOT_FOUND(404, "RankingProperty not found"),

    EXISTING_USER(409, "Existing User"),
    ALREADY_REPORTED(409, "Already reported"),
    NICKNAME_ALREADY_EXISTS(409, "Nickname already exists"),
    ID_DOES_NOT_MATCH(409, "Id does not match"),
    ROOM_NAME_ALREADY_EXISTS(409, "RoomName already exists"),
    CHAT_IN_ALREADY_EXISTS(409, "ChatIn already exists"),

    USER_RESIGNED(422, "Resigned User"),
    NOT_VALID_PASSWORD(404,"Not valid password"),
    CAN_NOT_UPDATE_PASSWORD(400,"Can not update password"),

    NOT_VALID_AUTH_KEY(404,"Not valid authKey"),
    FAIL_TO_SEND_EMAIL(400,"Fail to send an Email"),

    USER_UNAUTHORIZED(403, "Unauthorized user"),
    FILE_EMPTY(400, "file is empty"),
    FILE_NAME_NOT_VALID(400, "file name is not valid"),
    CONTENT_TYPE_NOT_ACCEPTABLE(400, "this content type is not acceptable"),
    FILE_NOT_FOUND(404, "file not found");

    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
