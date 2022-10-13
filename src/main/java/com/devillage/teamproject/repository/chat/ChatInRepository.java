package com.devillage.teamproject.repository.chat;


import com.devillage.teamproject.entity.ChatIn;
import com.devillage.teamproject.entity.ChatRoom;
import com.devillage.teamproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatInRepository extends JpaRepository<ChatIn, Long> {
    Optional<ChatIn> findFirstByUserAndChatroom(User user, ChatRoom chatRoom);
}
