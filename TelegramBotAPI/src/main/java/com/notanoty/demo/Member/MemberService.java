package com.notanoty.demo.Member;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatRepository;
import com.notanoty.demo.Genrealization.service.BaseService;
import com.notanoty.demo.Strike.Strike;
import com.notanoty.demo.Strike.StrikeDTO;
import com.notanoty.demo.Strike.StrikeRepository;
import com.notanoty.demo.User.User;
import com.notanoty.demo.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends BaseService<Member, Long> {

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
        return strikeRepository.save(strike);
    }
}
