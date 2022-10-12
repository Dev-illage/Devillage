package com.devillage.teamproject.controller.chat;

import com.devillage.teamproject.dto.ChatDto;
import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.security.resolver.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RequestMapping("/chat")
public interface ChatController {

    @GetMapping("/{room-name}")
    @ResponseStatus(HttpStatus.OK)
    ChatDto.DetailRoomDto getRoom(@AccessToken AuthDto.UserInfo userInfo,
                                  @PathVariable("room-name") String room);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ChatDto.SimpleRoomDto> getRooms();

}

