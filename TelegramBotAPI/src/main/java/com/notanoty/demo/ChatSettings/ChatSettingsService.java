package com.notanoty.demo.ChatSettings;

import com.notanoty.demo.Genrealization.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ChatSettingsService extends BaseService<ChatSettings, Long> {
    public ChatSettingsService(ChatSettingsRepository repository) {
        super(repository);
    }
}
