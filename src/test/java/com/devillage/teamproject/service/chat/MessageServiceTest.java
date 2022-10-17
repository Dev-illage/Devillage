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
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.devillage.teamproject.util.TestConstants.ID1;
import static com.devillage.teamproject.util.TestConstants.ID2;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest implements Reflection {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserService userService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatInRepository chatInRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    String existRoomName = "스프링";
    String notExistRoomName = "ㅅ프링";
    MessageType chatType = MessageType.CHAT;
    MessageType joinType = MessageType.JOIN;
    MessageType leaveType = MessageType.LEAVE;
    User user1 = newInstance(User.class);
    ;
    User user2 = newInstance(User.class);
    ;
    String content = "안녕하세요.";
    String notAuthorizedToken = "notAuthorizedToken";
    String notInChatInToken = "notInChatToken";
    String token = "token";
    SimpMessageHeaderAccessor notInHeaderAccessor = SimpMessageHeaderAccessor.create();
    SimpMessageHeaderAccessor notAuthorizedHeaderAccessor = SimpMessageHeaderAccessor.create();
    SimpMessageHeaderAccessor notInChatInHeaderAccessor = SimpMessageHeaderAccessor.create();
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
    ChatRoom chatRoom = newInstance(ChatRoom.class);
    ChatIn chatIn = newInstance(ChatIn.class);

    MessageServiceTest() throws Exception {
        setField(user1, "id", ID1);
        setField(user2, "id", ID2);
        notAuthorizedHeaderAccessor.setNativeHeader("Authorization", notAuthorizedToken);
        notInChatInHeaderAccessor.setNativeHeader("Authorization", notInChatInToken);
        headerAccessor.setNativeHeader("Authorization", token);
    }

    @Test
    public void sendMessageAuth() throws Exception {
        // given
        given(chatRepository.save(any(Chat.class)))
                .willAnswer(AdditionalAnswers.returnsFirstArg());
        given(jwtTokenUtil.getUserId(notAuthorizedToken)).
                willThrow(new BusinessLogicException(ExceptionCode.USER_AUTHORIZED));
        given(jwtTokenUtil.getUserId(notInChatInToken)).
                willReturn(user2.getId());
        given(jwtTokenUtil.getUserId(token)).
                willReturn(user1.getId());
        given(userService.findVerifiedUser(user1.getId()))
                .willReturn(user1);
        given(userService.findVerifiedUser(user2.getId()))
                .willReturn(user2);
        given(chatRoomRepository.findByRoomName(existRoomName))
                .willReturn(Optional.of(chatRoom));
        given(chatInRepository.findFirstByUserAndChatroom(user1, chatRoom))
                .willReturn(Optional.of(chatIn));
        given(chatInRepository.findFirstByUserAndChatroom(user2, chatRoom))
                .willReturn(Optional.empty());


        // when
        Chat chat = messageService.sendMessage(headerAccessor, existRoomName, chatType, content);

        // then
        assertThrows(BusinessLogicException.class,
                () -> messageService.sendMessage(headerAccessor, notExistRoomName, chatType, content));
        assertThrows(BusinessLogicException.class,
                () -> messageService.sendMessage(notInHeaderAccessor, existRoomName, chatType, content));
        assertThrows(BusinessLogicException.class,
                () -> messageService.sendMessage(notAuthorizedHeaderAccessor, existRoomName, chatType, content));
        assertThrows(BusinessLogicException.class,
                () -> messageService.sendMessage(notInChatInHeaderAccessor, existRoomName, chatType, content));
    }

    @Test
    public void verifyUserChatRoom() throws Exception {
        // given
        given(chatInRepository.findFirstByUserAndChatroom(user1, chatRoom))
                .willReturn(Optional.of(chatIn));
        given(chatInRepository.findFirstByUserAndChatroom(user2, chatRoom))
                .willReturn(Optional.empty());

        Method verifyUserChatRoom = MessageServiceImpl.class
                .getDeclaredMethod("verifyUserChatRoom", User.class, ChatRoom.class);
        MessageServiceImpl messageServiceImpl =
                new MessageServiceImpl(jwtTokenUtil, userService, chatRoomRepository, chatRepository, chatInRepository);
        verifyUserChatRoom.setAccessible(true);

        // when
        verifyUserChatRoom.invoke(messageServiceImpl, user1, chatRoom);

        // then
        assertThrows(Exception.class,
                () -> verifyUserChatRoom.invoke(messageServiceImpl, user2, chatRoom));
    }

    @Test
    public void joinUserChatRoom() throws Exception {
        // given
        given(chatInRepository.findFirstByUserAndChatroom(user1, chatRoom))
                .willReturn(Optional.empty());
        given(chatInRepository.findFirstByUserAndChatroom(user2, chatRoom))
                .willReturn(Optional.of(chatIn));

        Method joinUserChatRoom = MessageServiceImpl.class
                .getDeclaredMethod("joinUserChatRoom", User.class, ChatRoom.class);
        MessageServiceImpl messageServiceImpl =
                new MessageServiceImpl(jwtTokenUtil, userService, chatRoomRepository, chatRepository, chatInRepository);
        joinUserChatRoom.setAccessible(true);

        // when
        joinUserChatRoom.invoke(messageServiceImpl, user1, chatRoom);

        // then
        assertThrows(Exception.class,
                () -> joinUserChatRoom.invoke(messageServiceImpl, user2, chatRoom));
    }

    @Test
    public void exitUserChatRoom() throws Exception {
        // given
        given(chatInRepository.findFirstByUserAndChatroom(user1, chatRoom))
                .willReturn(Optional.of(chatIn));
        given(chatInRepository.findFirstByUserAndChatroom(user2, chatRoom))
                .willReturn(Optional.empty());

        Method exitUserChatRoom = MessageServiceImpl.class
                .getDeclaredMethod("exitUserChatRoom", User.class, ChatRoom.class);
        MessageServiceImpl messageServiceImpl =
                new MessageServiceImpl(jwtTokenUtil, userService, chatRoomRepository, chatRepository, chatInRepository);
        exitUserChatRoom.setAccessible(true);

        // when
        exitUserChatRoom.invoke(messageServiceImpl, user1, chatRoom);

        // then
        assertThrows(Exception.class,
                () -> exitUserChatRoom.invoke(messageServiceImpl, user2, chatRoom));
    }

}