package com.notanoty.demo.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("is_admin")
    private Boolean isAdmin;

    @JsonProperty("can_add_tasks")
    private Boolean canAddTasks;

    @JsonProperty("can_edit_tasks")
    private Boolean canMakePolls;

    @JsonProperty("can_give_strikes_without_poll")
    private Boolean canGiveStrikesWithoutPoll;
}
