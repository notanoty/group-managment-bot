package com.notanoty.demo.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoleAssignmentDTO {
    private Long memberId;
    private Long roleId;
}
