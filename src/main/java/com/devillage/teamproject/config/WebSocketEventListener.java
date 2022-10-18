package com.devillage.teamproject.config;

import com.devillage.teamproject.dto.ChatDto;
import com.devillage.teamproject.entity.ChatIn;
import com.devillage.teamproject.entity.ChatRoom;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.MessageType;
import com.devillage.teamproject.entity.enums.RoleType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.chat.ChatInRepository;
import com.devillage.teamproject.repository.chat.ChatRoomRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.chat.MessageService;
import com.devillage.teamproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageService messageService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
        StompHeaderAccessor headerAccessor =
                StompHeaderAccessor.wrap(
                        (Message<?>) StompHeaderAccessor.wrap(
                                event.getMessage()).getHeader("simpConnectMessage"));

        Long userId = jwtTokenUtil.getUserId(
                Objects.requireNonNull(headerAccessor.getNativeHeader("Authorization")).get(0));
        User user = userService.findVerifiedUser(userId);

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        assert sessionAttributes != null;
        sessionAttributes.put("userId", userId);
        sessionAttributes.put("nickName", user.getNickName());
        sessionAttributes.put("roomName", "public");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("Disconnect Check!");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println(headerAccessor);

        Long userId = (Long) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("userId");
        String nickName = (String) headerAccessor.getSessionAttributes().get("nickName");
        String roomName = (String) headerAccessor.getSessionAttributes().get("roomName");

        User user = userService.findVerifiedUser(userId);
        String accessToken =
                jwtTokenUtil.createAccessToken(user.getEmail(), user.getId(), List.of(RoleType.ROLE_USER.toString()));

        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.create();
        simpMessageHeaderAccessor.setNativeHeader("Authorization", "Bearer " + accessToken);

        messageService.sendMessage(simpMessageHeaderAccessor, roomName, MessageType.LEAVE, nickName + "님이 나갔습니다.");

        ChatDto chatMessage = new ChatDto(MessageType.LEAVE, nickName, nickName + "님이 나갔습니다.", LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }

}
