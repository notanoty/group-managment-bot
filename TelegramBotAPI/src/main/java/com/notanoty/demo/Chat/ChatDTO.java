package com.notanoty.demo.Chat;

import lombok.Data;

@Data
public class ChatDTO {
    private Long telegramChatId;
    private String chatName;

    public ChatDTO(Chat chat) {
        this.telegramChatId = chat.getChatId();
        this.chatName = chat.getName();
    }
}
