package com.notanoty.demo.Strike;

import com.notanoty.demo.Genrealization.controller.BaseController;
import com.notanoty.demo.Member.Member;
import com.notanoty.demo.Member.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strike")
public class StrikeController extends BaseController<Strike, Long>
{
    public StrikeController(StrikeService service)
    {
        super(service);
    }
}