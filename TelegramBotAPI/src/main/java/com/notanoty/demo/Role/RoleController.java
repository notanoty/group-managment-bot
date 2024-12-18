package com.notanoty.demo.Role;

import com.notanoty.demo.Genrealization.controller.BaseController;
import com.notanoty.demo.Member.Member;
import com.notanoty.demo.Member.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<Role, Long>
{
    public RoleController(RoleService service)
    {
        super(service);
    }
}