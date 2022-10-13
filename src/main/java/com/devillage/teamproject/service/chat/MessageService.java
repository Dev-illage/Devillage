package com.devillage.teamproject.service.chat;

import com.devillage.teamproject.entity.Chat;
import com.devillage.teamproject.entity.enums.MessageType;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface MessageService {
    Chat sendMessage(SimpMessageHeaderAccessor headerAccessor, String roomName, MessageType messageType, String content);
}
