package com.notanoty.demo.Strike;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StrikeDTO {
    private String reason;
    private LocalDate dateOfIssue;
}
