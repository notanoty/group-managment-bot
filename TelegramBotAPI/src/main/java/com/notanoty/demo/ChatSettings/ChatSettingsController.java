package com.notanoty.demo.ChatSettings;

import com.notanoty.demo.Genrealization.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-settings")
public class ChatSettingsController extends BaseController<ChatSettings, Long>
{
    public ChatSettingsController(ChatSettingsService service)
    {
        super(service);
    }
}
