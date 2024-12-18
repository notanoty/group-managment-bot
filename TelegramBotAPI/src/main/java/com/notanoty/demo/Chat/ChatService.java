package com.notanoty.demo.Chat;

import com.notanoty.demo.Genrealization.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ChatService extends BaseService<Chat, Long> {
    public ChatService(ChatRepository repository) {
        super(repository);
    }
}
