package com.notanoty.demo.Member;

import com.notanoty.demo.Chat.Chat;
import com.notanoty.demo.Chat.ChatService;
import com.notanoty.demo.Genrealization.controller.BaseController;
import com.notanoty.demo.Strike.Strike;
import com.notanoty.demo.Strike.StrikeDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping("/{id}/strike")
    public Strike giveStrike(@PathVariable Long id, @RequestBody StrikeDTO reason) {
        return ((MemberService) getService()).giveStrike(id, reason);
    }
}
