package com.notanoty.demo.Member;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatRepository;
import com.notanoty.demo.Genrealization.service.BaseService;
import com.notanoty.demo.Role.Role;
import com.notanoty.demo.Role.RoleAssignmentDTO;
import com.notanoty.demo.Role.RoleRepository;
import com.notanoty.demo.Strike.Strike;
import com.notanoty.demo.Strike.StrikeDTO;
import com.notanoty.demo.Strike.StrikeRepository;
import com.notanoty.demo.User.User;
import com.notanoty.demo.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService extends BaseService<Member, Long> {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StrikeRepository strikeRepository;

    public MemberService(MemberRepository repository) {
        super(repository);
    }

    public Strike giveStrike(Long memberId, StrikeDTO strikeDTO) {
        System.out.println(memberId + " " + strikeDTO.getReason());
        Member member = super.getRepository().findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Strike strike = new Strike();
        strike.setMember(member);
        strike.setReason(strikeDTO.getReason());
        strike.setDateOfIssue(strikeDTO.getDateOfIssue());
        return strikeRepository.save(strike);
    }

    public void assignRole(RoleAssignmentDTO roleAssignmentDTO) {
        Member member = super.getRepository().findById(roleAssignmentDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Role role = roleRepository.findById(roleAssignmentDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        member.setRole(role);
        super.getRepository().save(member);
    }


}
