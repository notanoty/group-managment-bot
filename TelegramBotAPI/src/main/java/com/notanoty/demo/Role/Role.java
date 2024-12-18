package com.notanoty.demo.Role;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, unique = true) // Explicit settings
    private Long roleId;

    private String name;
    private String description;
    private Boolean isAdmin;
    private Boolean canAddTasks;
    private Boolean canMakePolls;
    private Boolean canGiveStrikesWithoutPoll;
}
