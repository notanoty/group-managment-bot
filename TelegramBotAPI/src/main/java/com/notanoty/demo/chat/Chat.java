package com.notanoty.demo.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.notanoty.demo.strike.Strike;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("members_count")
    private Integer membersCount;

    @JsonProperty("bot_count")
    private Integer botCount;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Strike> strikes;


}
