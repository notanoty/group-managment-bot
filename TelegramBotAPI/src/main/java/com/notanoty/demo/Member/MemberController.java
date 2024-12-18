package com.notanoty.demo.Member;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatService;
import com.notanoty.demo.Genrealization.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController extends BaseController<Member, Long>
{
    public MemberController(MemberService service)
    {
        super(service);
    }
}
