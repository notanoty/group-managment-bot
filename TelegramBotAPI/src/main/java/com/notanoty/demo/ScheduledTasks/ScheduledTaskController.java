package com.notanoty.demo.ScheduledTasks;

import com.notanoty.demo.Genrealization.controller.BaseController;
import com.notanoty.demo.Member.Member;
import com.notanoty.demo.Member.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduled-task")
public class ScheduledTaskController extends BaseController<ScheduledTask, Long>
{
    public ScheduledTaskController(ScheduledTaskService service)
    {
        super(service);
    }
}