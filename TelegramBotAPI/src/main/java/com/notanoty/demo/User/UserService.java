package com.notanoty.demo.User;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatRepository;
import com.notanoty.demo.Genrealization.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User, Long> {
    public UserService(UserRepository repository) {
        super(repository);
    }
}
