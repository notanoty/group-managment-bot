package com.notanoty.demo.ScheduledTasks;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatRepository;
import com.notanoty.demo.Genrealization.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService extends BaseService<ScheduledTask, Long> {
    public ScheduledTaskService(ScheduledTasksRepository repository) {
        super(repository);
    }
}
