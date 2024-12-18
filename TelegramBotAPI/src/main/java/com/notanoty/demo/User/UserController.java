package com.notanoty.demo.User;

import com.notanoty.demo.Genrealization.controller.BaseController;
import com.notanoty.demo.Member.Member;
import com.notanoty.demo.Member.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController<User, Long>
{
    public UserController(UserService service)
    {
        super(service);
    }
}