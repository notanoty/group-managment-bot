package com.notanoty.demo.Chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.notanoty.demo.ChatSettings.ChatSettings;
import com.notanoty.demo.Member.Member;
import com.notanoty.demo.ScheduledTasks.ScheduledTask;
import com.notanoty.demo.Strike.Strike;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "chat_id")
    private Long chatId;

    private String name;

    @Column(name = "members_count")
    private Integer membersCount;

    @Column(name = "bot_count")
    private Integer botCount;

    @OneToOne
    @JoinColumn(name = "chat_settings_id")
    private ChatSettings chatSettings;

    @OneToMany(mappedBy = "chat")
    @JsonManagedReference
    private List<ScheduledTask> scheduledTasks;

//    @OneToMany(mappedBy = "chat")
//    @JsonManagedReference
//    private List<Member> members;

    @OneToMany(mappedBy = "chat")
    @JsonManagedReference
    private List<Strike> strikes;



}
