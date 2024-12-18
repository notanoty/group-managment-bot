package com.notanoty.demo.ChatSettings;

import com.notanoty.demo.Chat.Chat;
import jakarta.persistence.*;

@Entity
@Table(name = "chat_settings")
public class ChatSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;



    @Column(name = "is_bot_enabled")
    private Boolean isBotEnabled;

    @Column(name = "is_strike_enabled")
    private Boolean isStrikeEnabled;

    @Column(name = "is_scheduled_tasks_enabled")
    private Boolean isScheduledTasksEnabled;

    @Column(name = "is_polls_enabled")
    private Boolean canMembersMakePolls;



}
