package com.devillage.teamproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ChatRoom extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="chatroom_id")
    private Long id;

    private String roomName;

    @OneToMany(mappedBy = "chatroom")
    private final List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "chatroom")
    private final List<ChatIn> chatIns = new ArrayList<>();

    public ChatRoom(String roomName) {
        this.roomName = roomName;
    }
}
