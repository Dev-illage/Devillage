package com.devillage.teamproject.repository.chat;

import com.devillage.teamproject.entity.ChatRoom;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChatRoomRepositoryTest implements Reflection {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    public void findByRoomName() throws Exception {
        // given
        ChatRoom chatRoom1 = newInstance(ChatRoom.class);
        setField(chatRoom1, "roomName", "스프링");
        ChatRoom chatRoom2 = newInstance(ChatRoom.class);
        setField(chatRoom2, "roomName", "리액트");

        chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2));

        // when
        Optional<ChatRoom> spring = chatRoomRepository.findByRoomName("스프링");
        Optional<ChatRoom> react = chatRoomRepository.findByRoomName("리액트");

        // then
        assertThat(spring.isPresent()).isTrue();
        assertThat(spring.get().getRoomName()).isEqualTo("스프링");
        assertThat(react.isPresent()).isTrue();
        assertThat(react.get().getRoomName()).isEqualTo("리액트");
    }

}
