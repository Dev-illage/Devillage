package com.devillage.teamproject.service.chat;

import com.devillage.teamproject.entity.ChatIn;
import com.devillage.teamproject.entity.ChatRoom;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.chat.ChatInRepository;
import com.devillage.teamproject.repository.chat.ChatRoomRepository;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.devillage.teamproject.util.TestConstants.ID1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest implements Reflection {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatInRepository chatInRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    public void getRoom() throws Exception {
        // given
        User user = newInstance(User.class);
        setField(user, "id", ID1);
        String existRoomName1 = "스프링";
        String existRoomName2 = "리액트";
        String notExistRoomName = "ㅅ프링";

        ChatRoom chatRoomIncludeUser = newInstance(ChatRoom.class);
        ChatRoom chatRoomNotIncludeUser = newInstance(ChatRoom.class);
        ChatIn chatIn1 = newInstance(ChatIn.class);
        ChatIn chatIn2 = newInstance(ChatIn.class);
        setField(chatRoomIncludeUser, "chatIns", List.of(chatIn1));
        setField(chatRoomNotIncludeUser, "chatIns", List.of(chatIn2));
        setField(chatIn1, "user", user);
        setField(chatIn2, "user", newInstance(User.class));

        given(userService.findVerifiedUser(user.getId()))
                .willReturn(user);
        given(chatRoomRepository.findByRoomName(existRoomName1))
                .willReturn(Optional.of(chatRoomIncludeUser));
        given(chatRoomRepository.findByRoomName(existRoomName2))
                .willReturn(Optional.of(chatRoomNotIncludeUser));
        given(chatRoomRepository.findByRoomName(notExistRoomName))
                .willReturn(Optional.empty());

        // when
        ChatRoom room = chatService.getRoom(user.getId(), existRoomName1);

        // then
        assertThat(room).isEqualTo(chatRoomIncludeUser);
        assertThrows(BusinessLogicException.class,
                () -> chatService.getRoom(user.getId(), existRoomName2));
        assertThrows(BusinessLogicException.class,
                () -> chatService.getRoom(user.getId(), notExistRoomName));
    }

    @Test
    public void postRoom() throws Exception {
        // given
        User user = newInstance(User.class);
        setField(user, "id", ID1);
        String existRoomName = "스프링";
        String notExistRoomName = "리액트";

        given(userService.findVerifiedUser(user.getId()))
                .willReturn(user);
        given(chatRoomRepository.existsByRoomName(existRoomName))
                .willReturn(true);
        given(chatRoomRepository.existsByRoomName(notExistRoomName))
                .willReturn(false);
        given(chatRoomRepository.save(any(ChatRoom.class)))
                .willAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        ChatRoom chatRoom = chatService.postRoom(user.getId(), notExistRoomName);

        // then
        assertThat(chatRoom.getRoomName()).isEqualTo(notExistRoomName);
        assertThrows(BusinessLogicException.class,
                () -> chatService.postRoom(user.getId(), existRoomName));
    }

}
