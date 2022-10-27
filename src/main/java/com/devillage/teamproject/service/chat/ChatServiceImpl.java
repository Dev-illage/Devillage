package com.devillage.teamproject.service.chat;

import com.devillage.teamproject.entity.ChatIn;
import com.devillage.teamproject.entity.ChatRoom;
import com.devillage.teamproject.repository.chat.ChatInRepository;
import com.devillage.teamproject.repository.chat.ChatRoomRepository;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatInRepository chatInRepository;

    @Override
    public List<ChatRoom> getRooms() {
        return chatRoomRepository.findAll();
    }

    @Override
    public ChatRoom getRoom(Long userId, String roomName) {
        User user = userService.findVerifiedUser(userId);
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHAT_ROOM_NOT_FOUND));

        List<User> users = chatRoom.getChatIns().stream()
                .map(ChatIn::getUser)
                .collect(Collectors.toList());

        if (!users.contains(user)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_CHATROOM_EXCEPTION);
        }

        return chatRoom;
    }

    @Override
    @Transactional
    public ChatRoom postRoom(Long userId, String roomName) {
        User user = userService.findVerifiedUser(userId);
        if (chatRoomRepository.existsByRoomName(roomName)) {
            throw new BusinessLogicException(ExceptionCode.ROOM_NAME_ALREADY_EXISTS);
        }
        ChatRoom chatRoom = new ChatRoom(roomName);
        ChatIn chatIn = new ChatIn(user, chatRoom);
        chatInRepository.save(chatIn);

        return chatRoomRepository.save(chatRoom);
    }
}
