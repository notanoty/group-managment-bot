package com.notanoty.demo.Member;

import com.fasterxml.jackson.annotation.*;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "memberId")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_group_name")
    private String memberGroupName;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private Set<Strike> strikes;


    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnoreProperties("members")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
