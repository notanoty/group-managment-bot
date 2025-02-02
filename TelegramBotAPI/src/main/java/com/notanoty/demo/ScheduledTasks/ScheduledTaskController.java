package com.notanoty.demo.ScheduledTasks;

import com.notanoty.demo.Genrealization.controller.BaseController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduled-task")
public class ScheduledTaskController extends BaseController<ScheduledTask, Long>
{
    public ScheduledTaskController(ScheduledTaskService service)
    {
        super(service);
    }

}