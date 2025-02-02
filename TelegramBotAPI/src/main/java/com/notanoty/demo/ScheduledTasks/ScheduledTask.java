package com.notanoty.demo.ScheduledTasks;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.notanoty.demo.Chat.Chat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "scheduled_task")
@NoArgsConstructor
public class ScheduledTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduled_task_id")
    private Long scheduledTasksChatId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "date_of_expire")
    private LocalDate dateOfExpire;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private Chat chat;

}
