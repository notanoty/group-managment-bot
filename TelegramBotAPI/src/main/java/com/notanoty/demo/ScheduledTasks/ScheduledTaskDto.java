package com.notanoty.demo.ScheduledTasks;

import com.notanoty.demo.Chat.ChatDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduledTaskDto {
    private Long scheduledTasksChatId;
    private String taskName;
    private String taskDescription;
    private LocalDate dateOfExpire;
    private ChatDto chat;

    public ScheduledTaskDto(ScheduledTask task) {
        this.scheduledTasksChatId = task.getScheduledTasksChatId();
        this.taskName = task.getTaskName();
        this.taskDescription = task.getTaskDescription();
        this.dateOfExpire = task.getDateOfExpire();
        this.chat = task.getChat() != null ? new ChatDto(task.getChat()) : null;
    }
}
