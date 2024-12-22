package com.notanoty.demo.Strike;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatDto;
import com.notanoty.demo.Member.Member;
import com.notanoty.demo.Member.MemberDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "strikes")
public class Strike {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strikeId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public MemberDto getMember() {
        return new MemberDto(member);
    }

    public ChatDto getChat() {
        return new ChatDto(chat);
    }

}
