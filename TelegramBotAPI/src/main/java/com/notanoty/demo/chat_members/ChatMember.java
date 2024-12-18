package com.notanoty.demo.chatmembers;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.notanoty.demo.chat.Chat;
import com.notanoty.demo.chat_members.ChatMemberId;
import com.notanoty.demo.member.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "chat_members")
public class ChatMember {

    @EmbeddedId
    @JsonProperty("id")
    private ChatMemberId id;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    @JsonProperty("chatId")
    @JsonBackReference("chat-members")
    private Chat chat;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    @JsonProperty("memberId")
    @JsonBackReference("member-chats")
    private Member member;

    @JsonProperty("joinedAt")
    @Column(name = "joined_at")
    private LocalDate joinedAt;

    @JsonProperty("leftAt")
    @Column(name = "left_at")
    private LocalDate leftAt;
}
