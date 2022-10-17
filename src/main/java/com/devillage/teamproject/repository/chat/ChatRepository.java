package com.devillage.teamproject.repository.chat;

import com.devillage.teamproject.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
