package com.notanoty.demo.ScheduledTasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ScheduledTaskDTO {
    @NotBlank(message = "Task name cannot be blank")
    private String taskName;

    @NotBlank(message = "Task description cannot be blank")
    private String taskDescription;

    @NotNull(message = "Date of expire cannot be null")
    private LocalDate dateOfExpire;
}
