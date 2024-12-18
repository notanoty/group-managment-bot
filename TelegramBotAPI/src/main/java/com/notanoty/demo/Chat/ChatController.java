package com.notanoty.demo.Chat;

import com.notanoty.demo.Genrealization.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController<Chat, Long>
{
    public ChatController(ChatService service)
    {
        super(service);
    }


}
