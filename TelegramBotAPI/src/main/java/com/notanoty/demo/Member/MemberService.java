package com.notanoty.demo.Member;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatRepository;
import com.notanoty.demo.Genrealization.service.BaseService;
import com.notanoty.demo.User.User;
import com.notanoty.demo.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends BaseService<Member, Long> {

    public MemberService(MemberRepository repository) {
        super(repository);
    }
}
