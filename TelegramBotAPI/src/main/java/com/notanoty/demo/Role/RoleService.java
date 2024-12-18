package com.notanoty.demo.Role;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatRepository;
import com.notanoty.demo.Genrealization.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, Long> {
    public RoleService(RoleRepository repository) {
        super(repository);
    }
}
