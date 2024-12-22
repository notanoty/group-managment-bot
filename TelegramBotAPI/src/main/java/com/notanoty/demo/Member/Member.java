package com.notanoty.demo.Member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Role.Role;
import com.notanoty.demo.Strike.Strike;
import com.notanoty.demo.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

//    @OneToMany(mappedBy = "member")
//    @JoinColumn(name = "strikes_id")
//    private Set<Strike> strikes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
