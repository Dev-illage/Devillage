package com.devillage.teamproject.service.chat;

import com.devillage.teamproject.entity.ChatRoom;

import java.util.List;

public interface ChatService {
    List<ChatRoom> getRooms();
    ChatRoom getRoom(Long userId, String roomName);
    ChatRoom postRoom(Long userId, String roomName);
}
