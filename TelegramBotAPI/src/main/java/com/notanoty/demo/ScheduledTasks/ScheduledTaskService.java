package com.notanoty.demo.ScheduledTasks;

import com.notanoty.demo.Genrealization.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledTaskService extends BaseService<ScheduledTask, Long> {
    public ScheduledTaskService(ScheduledTasksRepository repository) {
        super(repository);
    }


    public List<ScheduledTaskReturnDTO> getAllScheduledTasks() {
        List<ScheduledTask> tasks = super.findAll();
        return tasks.stream()
                .map(ScheduledTaskReturnDTO::new)
                .toList();
    }
}
