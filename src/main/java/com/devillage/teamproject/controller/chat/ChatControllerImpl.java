package com.devillage.teamproject.controller.chat;

import com.devillage.teamproject.dto.ChatDto;
import com.devillage.teamproject.entity.ChatRoom;
import com.devillage.teamproject.service.chat.ChatService;
import com.devillage.teamproject.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatControllerImpl implements ChatController {
    private final ChatService chatService;

    @Override
    public ChatDto.DetailRoomDto getRoom(AuthDto.UserInfo userInfo, String roomName) {
        ChatRoom room = chatService.getRoom(userInfo.getId(), roomName);
        return new ChatDto.DetailRoomDto(
                room.getRoomName(),
                room.getChatIns().stream()
                        .map(chatIn -> new ChatDto.UserDto(chatIn.getUser().getNickName()))
                        .collect(Collectors.toList()),
                room.getChats().stream()
                        .map(chat -> new ChatDto(
                                chat.getMessageType(),
                                chat.getUser().getNickName(),
                                chat.getContent(),
                                chat.getCreatedAt()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<ChatDto.SimpleRoomDto> getRooms() {
        List<ChatRoom> rooms = chatService.getRooms();
        return rooms.stream()
                .map(room -> new ChatDto.SimpleRoomDto(room.getRoomName(), (long) room.getChatIns().size()))
                .collect(Collectors.toList());
    }

}
