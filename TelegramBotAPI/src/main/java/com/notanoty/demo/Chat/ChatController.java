package com.notanoty.demo.Chat;

import com.notanoty.demo.Genrealization.APIResponse.ApiResponse;
import com.notanoty.demo.Genrealization.controller.BaseController;
import com.notanoty.demo.ScheduledTasks.ScheduledTask;
import com.notanoty.demo.ScheduledTasks.ScheduledTaskDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController<Chat, Long> {
    public ChatController(ChatService service) {
        super(service);
    }

    @PostMapping("/{id}/scheduled-task")
    public ResponseEntity<ApiResponse<ScheduledTask>> startScheduledTask(@PathVariable Long id, @Valid @RequestBody ScheduledTaskDTO task) {
        try {
            return ApiResponse.successCreated(((ChatService) getService()).addTask(id, task), "Task added");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), "Failed to add task", HttpStatus.BAD_REQUEST);
        }

    }
}