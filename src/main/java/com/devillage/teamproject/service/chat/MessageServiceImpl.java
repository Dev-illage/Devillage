package com.devillage.teamproject.service.chat;

import com.devillage.teamproject.entity.Chat;
import com.devillage.teamproject.entity.ChatIn;
import com.devillage.teamproject.entity.ChatRoom;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.MessageType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.chat.ChatInRepository;
import com.devillage.teamproject.repository.chat.ChatRepository;
import com.devillage.teamproject.repository.chat.ChatRoomRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatInRepository chatInRepository;

    @Override
    public Chat sendMessage(SimpMessageHeaderAccessor headerAccessor,
                            String roomName, MessageType messageType, String content) {
        List<String> authorization = headerAccessor.getNativeHeader("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_CHATROOM_EXCEPTION);
        }

        String token = authorization.get(0);
        Long userId = jwtTokenUtil.getUserId(token);
        User user = userService.findVerifiedUser(userId);

        ChatRoom chatRoom = chatRoomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHAT_ROOM_NOT_FOUND));

        if (messageType.equals(MessageType.JOIN)) {
            joinUserChatRoom(user, chatRoom);
        } else if (messageType.equals(MessageType.LEAVE)) {
            exitUserChatRoom(user, chatRoom);
        } else if (messageType.equals(MessageType.CHAT)) {
            verifyUserChatRoom(user, chatRoom);
        }

        return chatRepository.save(new Chat(user.getNickName(), messageType, content, chatRoom, user));
    }

    private void verifyUserChatRoom(User user, ChatRoom chatRoom) {
        chatInRepository.findFirstByUserAndChatroom(user, chatRoom)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_CHATROOM_EXCEPTION));
    }

    private void joinUserChatRoom(User user, ChatRoom chatRoom) {
        chatInRepository.findFirstByUserAndChatroom(user, chatRoom)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHAT_IN_ALREADY_EXISTS));
        chatInRepository.save(new ChatIn(user, chatRoom));
    }

    private void exitUserChatRoom(User user, ChatRoom chatRoom) {
        ChatIn chatIn = chatInRepository.findFirstByUserAndChatroom(user, chatRoom)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHAT_IN_NOT_FOUND));
        chatInRepository.delete(chatIn);
    }

}
