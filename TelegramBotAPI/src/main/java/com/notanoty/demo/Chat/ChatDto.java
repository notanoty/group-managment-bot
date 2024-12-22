package com.notanoty.demo.Chat;

import lombok.Data;

@Data
public class ChatDto {
    private Long chatId;
    private String chatName;

    public ChatDto(Chat chat) {
        this.chatId = chat.getChatId();
        this.chatName = chat.getName();
    }
}
