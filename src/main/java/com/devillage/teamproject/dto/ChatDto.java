package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ChatDto {
    private MessageType type;
    private String sender;
    private String content;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class UserDto {
        private String username;
    }

    @Getter
    @AllArgsConstructor
    public static class SimpleRoomDto {
        private String roomName;
        private Long numberOfUser;
    }

    @Getter
    @AllArgsConstructor
    public static class DetailRoomDto {
        private String roomName;
        private List<UserDto> users;
        private List<ChatDto> chats;
    }
}
