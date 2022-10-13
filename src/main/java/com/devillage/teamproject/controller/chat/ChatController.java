package com.devillage.teamproject.controller.chat;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.ChatDto;
import com.devillage.teamproject.security.resolver.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/chat")
public interface ChatController {

    @GetMapping("/{room-name}")
    @ResponseStatus(HttpStatus.OK)
    ChatDto.DetailRoomDto getRoom(@AccessToken AuthDto.UserInfo userInfo,
                                  @PathVariable("room-name") String roomName);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ChatDto.SimpleRoomDto> getRooms();

    @PostMapping("/{room-name}")
    String postRoom(@AccessToken AuthDto.UserInfo userInfo,
                    @PathVariable("room-name") String roomName);
}

