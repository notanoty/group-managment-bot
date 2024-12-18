package com.notanoty.demo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.notanoty.demo.member.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")  // Escape reserved keyword
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // Explicit column name
    @JsonProperty("user_id")
    private Long userId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Member> members;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

}
