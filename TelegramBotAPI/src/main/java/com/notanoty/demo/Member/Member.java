package com.notanoty.demo.Member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Strike.Strike;
import com.notanoty.demo.User.User;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @OneToMany
    @JoinColumn(name = "member_id")
    private Set<Strike> strikes;


}
