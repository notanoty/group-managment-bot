package com.notanoty.demo.Member;

import com.notanoty.demo.Genrealization.APIResponse.ApiResponse;
import com.notanoty.demo.Genrealization.controller.BaseController;
import com.notanoty.demo.Role.RoleAssignmentDTO;
import com.notanoty.demo.Strike.Strike;
import com.notanoty.demo.Strike.StrikeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController extends BaseController<Member, Long> {
    public MemberController(MemberService service) {
        super(service);
    }

    @RequestMapping("/{id}/strike")
    public Strike giveStrike(@PathVariable Long id, @RequestBody StrikeDTO strikeDTO) {
        return ((MemberService) getService()).giveStrike(id, strikeDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<MemberDto>>> getAllMembers() {
        List<MemberDto> entities = getService().findAll().stream().map(MemberDto::new).toList();
        return ApiResponse.success(entities, "Fetched all entities successfully.");
    }

    @PostMapping("/role-assignment")
    public ResponseEntity<ApiResponse<MemberDto>> assignRole(@RequestBody RoleAssignmentDTO roleAssignmentDTO) {
        ((MemberService) getService()).assignRole(roleAssignmentDTO);
        return ApiResponse.successNoContent("Role assigned successfully.");
    }
}