package com.notanoty.demo.Strike;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatRepository;
import com.notanoty.demo.Genrealization.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class StrikeService extends BaseService<Strike, Long> {
    public StrikeService(StrikeRepository repository) {
        super(repository);
    }
}
