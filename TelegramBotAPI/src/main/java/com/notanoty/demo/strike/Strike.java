package com.notanoty.demo.strike;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.notanoty.demo.chat.Chat;
import com.notanoty.demo.member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "strikes")
public class Strike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strikeId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, insertable = false, updatable = false)
    @JsonProperty("member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false, insertable = false, updatable = false)
    @JsonProperty("chat_id")
    private Chat chat;
}
