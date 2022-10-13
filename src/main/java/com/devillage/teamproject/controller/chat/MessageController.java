package com.devillage.teamproject.controller.chat;

import com.devillage.teamproject.dto.ChatDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

@MessageMapping("/message")
public interface MessageController {

    @MessageMapping("/{roomName}")
    @SendTo("/topic/{roomName}")
    ChatDto sendMessage(SimpMessageHeaderAccessor headerAccessor,
                        @Payload ChatDto.SocketRequestDto requestDto,
                        String roomName);

}
