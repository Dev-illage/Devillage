package com.devillage.teamproject.entity;

import com.devillage.teamproject.entity.enums.MessageType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Chat extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="chat_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String nickName;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chatroom_id")
    private ChatRoom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Deprecated
    public void setDate() {
        setCreatedAt(LocalDateTime.now());
        setLastModifiedAt(LocalDateTime.now());
    }

    public Chat(String nickName, MessageType messageType, String content, ChatRoom chatroom, User user) {
        this.nickName = nickName;
        this.messageType = messageType;
        this.content = content;
        this.chatroom = chatroom;
        this.user = user;
    }
}
