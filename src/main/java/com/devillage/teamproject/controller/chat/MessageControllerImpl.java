package com.devillage.teamproject.controller.chat;

import com.devillage.teamproject.dto.ChatDto;
import com.devillage.teamproject.entity.Chat;
import com.devillage.teamproject.service.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageControllerImpl implements MessageController {
    private final MessageService messageService;

    public ChatDto sendMessage(SimpMessageHeaderAccessor headerAccessor,
                               ChatDto.SocketRequestDto requestDto,
                               @DestinationVariable String roomName) {
        Chat chat = messageService.sendMessage(headerAccessor, roomName, requestDto.getMessageType(), requestDto.getContent());

        return new ChatDto(
                chat.getMessageType(),
                chat.getNickName(),
                chat.getContent(),
                chat.getCreatedAt()
        );
    }

}