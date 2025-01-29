package com.notanoty.demo.ScheduledTasks;

import com.notanoty.demo.Genrealization.controller.BaseController;
import com.notanoty.demo.Genrealization.service.ServiceBase;
import com.notanoty.demo.Member.Member;
import com.notanoty.demo.Member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

@RestController
@RequestMapping("/scheduled-task")
public class ScheduledTaskController extends BaseController<ScheduledTask, Long>
{
    public ScheduledTaskController(ScheduledTaskService service)
    {
        super(service);
    }

//    @GetMapping
//    public ResponseEntity<List<ScheduledTaskDto>>  getAll() {
//        List<ScheduledTaskDto> taskDtos = scheduledTaskService.getAllScheduledTasks();
//        return ResponseEntity.ok(taskDtos);
//    }
//
//    @PostMapping
//    public ScheduledTask create(@RequestBody ScheduledTask entity) {
//        return scheduledTaskService.save(entity);
//    }
//
//    @PutMapping("/{id}")
//    public ScheduledTask update(@PathVariable Long id, @RequestBody ScheduledTask entity) {
//        return scheduledTaskService.update(id, entity);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        scheduledTaskService.deleteById(id);
//    }
//
//    public ServiceBase<ScheduledTask, Long> getService() {
//        return scheduledTaskService;
//    }
}