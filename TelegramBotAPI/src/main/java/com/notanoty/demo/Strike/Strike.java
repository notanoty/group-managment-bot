package com.notanoty.demo.Strike;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "strikes")
public class Strike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strikeId;


}
