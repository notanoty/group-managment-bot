package com.notanoty.demo.Chat;

import com.notanoty.demo.Genrealization.service.BaseService;
import com.notanoty.demo.ScheduledTasks.ScheduledTask;
import com.notanoty.demo.ScheduledTasks.ScheduledTaskDTO;
import com.notanoty.demo.ScheduledTasks.ScheduledTasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService extends BaseService<Chat, Long> {
    @Autowired
    ScheduledTasksRepository scheduledTasksRepository;

    public ChatService(ChatRepository repository) {
        super(repository);
    }

    public ScheduledTask addTask(Long id, ScheduledTaskDTO task) {
        Chat chat = super.findById(id).orElseThrow();
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.setChat(chat);
        scheduledTask.setTaskName(task.getTaskName());
        scheduledTask.setTaskDescription(task.getTaskDescription());
        scheduledTask.setDateOfExpire(task.getDateOfExpire());

        return scheduledTasksRepository.save(scheduledTask);
    }
}
