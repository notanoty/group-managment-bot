package com.notanoty.demo.ScheduledTasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledTasksRepository extends JpaRepository<ScheduledTask, Long> {
}
