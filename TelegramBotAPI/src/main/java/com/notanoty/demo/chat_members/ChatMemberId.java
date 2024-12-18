package com.notanoty.demo.chat_members;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class ChatMemberId implements Serializable {
    private Long chatId;
    private Long memberId;

    public ChatMemberId() {}

    public ChatMemberId(Long chatId, Long memberId) {
        this.chatId = chatId;
        this.memberId = memberId;
    }

    // Getters and setters if not using Lombok
}
