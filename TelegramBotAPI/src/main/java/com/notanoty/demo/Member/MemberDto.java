package com.notanoty.demo.Member;

import com.notanoty.demo.Role.Role;
import com.notanoty.demo.User.User;
import lombok.Data;

@Data
public class MemberDto {
    private Long memberId;
    private Role role;
    private User user;

    public MemberDto(Member member) {
        this.memberId = member.getMemberId();
        this.role = member.getRole();
        this.user = member.getUser();
    }

}
